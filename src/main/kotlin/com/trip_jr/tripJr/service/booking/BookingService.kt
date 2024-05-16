package com.trip_jr.tripJr.service.booking

import com.trip_jr.tripJr.controller.hotel.room.UpdateRoomDTO
import com.trip_jr.tripJr.dto.RoomDTO
import com.trip_jr.tripJr.dto.booking.BookingDTO
import com.trip_jr.tripJr.dto.booking.CreateBookingDTO
import com.trip_jr.tripJr.dto.booking.UpdateBookingDTO
import com.trip_jr.tripJr.dto.hotel.RateDTO
import com.trip_jr.tripJr.jooq.tables.references.BOOKING
import com.trip_jr.tripJr.jooq.tables.references.ROOM
import com.trip_jr.tripJr.service.hotel.HotelService
import com.trip_jr.tripJr.service.hotel.room.RoomService
import com.trip_jr.tripJr.service.utils.BookingUtils
import com.trip_jr.tripJr.service.utils.UUIDUtils
import org.jooq.DSLContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

import java.util.*


@Service
class BookingService {

    @Autowired
    lateinit var dslContext: DSLContext

    @Autowired
    lateinit var uuidUtils: UUIDUtils

    @Autowired
    lateinit var bookingUtils: BookingUtils

    @Autowired
    lateinit var roomService: RoomService

    private val logger = LoggerFactory.getLogger(HotelService::class.java)

    fun getAllBookingsByUserId(userId: UUID): List<BookingDTO> {
        try {
            val bookingsRecord = dslContext.select()
                .from(BOOKING)
                .join(ROOM).on(BOOKING.ROOM_ID.eq(ROOM.ROOM_ID))
                .where(BOOKING.USER_ID.eq(userId))
                .fetch()


            return bookingsRecord.into(BookingDTO::class.java)
        } catch (e: Exception) {
            throw e
        }
    }

    fun getSingleBookingByUserId(userId: UUID, bookingId: UUID): BookingDTO? {
        try {
            val bookingRecord = dslContext.select()
                .from(BOOKING)
                .join(ROOM).on(ROOM.ROOM_ID.eq(BOOKING.ROOM_ID))
                .where(BOOKING.USER_ID.eq(userId))
                .and(BOOKING.BOOKING_ID.eq(bookingId))
                .fetchOne()

            return bookingRecord?.into(BookingDTO::class.java)
        } catch (e: Exception) {
            throw e
        }
    }


    fun createBooking(booking: CreateBookingDTO): BookingDTO {
        try {
            val isRoomAvailable = bookingUtils.isRoomNotBooked(booking)
            if (!isRoomAvailable) {
                throw IllegalStateException("The selected room is not available for the specified dates.")
            }

            val roomId = booking.roomDetails.roomId
            val rateId = booking.roomDetails.rateId


            val totalCost = bookingUtils.calculateTotalCost(booking)
            val bookingId = booking.bookingId ?: uuidUtils.generateUUID()
            val currentTimestamp = OffsetDateTime.now(ZoneOffset.UTC)

            bookingUtils.changeRoomStatusWhenBooked(booking)

            val bookingRecord = dslContext.insertInto(BOOKING)
                .set(BOOKING.BOOKING_ID, bookingId)
                .set(BOOKING.USER_ID, booking.userId)
                .set(BOOKING.HOTEL_ID, booking.hotelId)
                .set(BOOKING.GUEST_FIRST_NAME, booking.guestFirstName)
                .set(BOOKING.GUEST_LAST_NAME, booking.guestLastName)
                .set(BOOKING.NUM_OF_GUESTS, booking.numOfGuests)
                .set(BOOKING.OCCASION, booking.occasion)
                .set(BOOKING.GUEST_NOTES, booking.guestNotes)
                .set(BOOKING.CHECK_IN_DATE, booking.checkInDate)
                .set(BOOKING.CHECK_OUT_DATE, booking.checkOutDate)
                .set(BOOKING.TOTAL_COST, totalCost)
                .set(BOOKING.ROOM_ID, roomId)
                .set(BOOKING.UPDATED_AT, currentTimestamp)
                .returningResult(BOOKING.BOOKING_ID)
                .fetchOne()

            bookingRecord ?: throw NullPointerException("Failed to create booking record!")

            val roomRecord = roomService.roomById(roomId)
            val createdBooking = BookingDTO(
                bookingId = bookingRecord.get(BOOKING.BOOKING_ID),
                userId = booking.userId,
                hotelId = booking.hotelId,
                guestFirstName = booking.guestFirstName,
                guestLastName = booking.guestLastName,
                numOfGuests = booking.numOfGuests,
                occasion = booking.occasion,
                guestNotes = booking.guestNotes,
                checkInDate = booking.checkInDate,
                checkOutDate = booking.checkOutDate,
                totalCost = totalCost,
                roomDetails = roomRecord?.let {
                    RoomDTO(
                        roomId = roomId,
                        hotelId = it.hotelId,
                        roomNumber = roomRecord.roomNumber,
                        roomType = roomRecord.roomType,
                        roomStatus = roomRecord.roomStatus,
                        bedType = roomRecord.bedType,
                        maximumOccupancy = roomRecord.maximumOccupancy,
                        description = roomRecord.description,
                        floor = roomRecord.floor,
                        availability = roomRecord.availability,
                        rate = roomRecord.rate?.let { it1 ->
                            RateDTO(
                                rateId = rateId,
                                rate = it1.rate,
                                month = roomRecord.rate.month,
                                defaultRate = roomRecord.rate.defaultRate
                            )
                        }
                    )
                },
                createdAt = currentTimestamp.toLocalDateTime(),
                updatedAt = currentTimestamp.toLocalDateTime()
            )


            return createdBooking
        } catch (e: Exception) {
            throw e
        }
    }


    fun updateBooking(userId: UUID, bookingId: UUID, booking: UpdateBookingDTO) : BookingDTO? {
        try {
            val bookingRecord = dslContext
                .select()
                .from(BOOKING)
                .where(BOOKING.USER_ID.eq(userId))
                .and(BOOKING.BOOKING_ID.eq(bookingId))
                .fetchOne() ?: throw RuntimeException("Booking with ID $bookingId not found")

            val roomId = bookingRecord.get(BOOKING.ROOM_ID)
            val roomRecord = roomId?.let { bookingUtils.getBookingsRoomById(it) }


            val hotelId = bookingRecord.get(BOOKING.HOTEL_ID)

            val originalBookingRecord : BookingDTO? = bookingRecord.get(BOOKING.GUEST_FIRST_NAME)?.let {
                bookingRecord.get(BOOKING.CHECK_IN_DATE)?.let { it1 ->
                    bookingRecord.get(BOOKING.NUM_OF_GUESTS)?.let { it2 ->
                        bookingRecord.get(BOOKING.CREATED_AT)?.toLocalDateTime()?.let { it3 ->
                            BookingDTO(
                                bookingId = bookingId,
                                hotelId = hotelId,
                                userId = userId,
                                guestFirstName = it,
                                guestLastName = bookingRecord.get(BOOKING.GUEST_LAST_NAME)!!,
                                numOfGuests = it2,
                                occasion = bookingRecord.get(BOOKING.OCCASION),
                                guestNotes = bookingRecord.get(BOOKING.GUEST_NOTES),
                                checkInDate = it1,
                                checkOutDate = bookingRecord.get(BOOKING.CHECK_OUT_DATE)!!,
                                totalCost = bookingRecord.get(BOOKING.TOTAL_COST),
                                createdAt = it3,
                                updatedAt = bookingRecord.get(BOOKING.UPDATED_AT)?.toLocalDateTime()!!,
                                roomDetails = roomRecord?.let { it4 ->
                                    RoomDTO(
                                        roomId = roomId,
                                        hotelId = it4.hotelId,
                                        rate = roomRecord.rate?.let { it5 ->
                                            RateDTO(
                                                rateId = roomRecord.rate.rateId,
                                                rate = it5.rate,
                                                month = roomRecord.rate.month,
                                                defaultRate = roomRecord.rate.defaultRate
                                            )
                                        },
                                        roomNumber = roomRecord.roomNumber,
                                        roomType = roomRecord.roomType,
                                        roomStatus = roomRecord.roomStatus,
                                        bedType = roomRecord.bedType,
                                        maximumOccupancy = roomRecord.maximumOccupancy,
                                        description = roomRecord.description,
                                        floor = roomRecord.floor,
                                        availability = roomRecord.availability,
                                        lastCleaned = roomRecord.lastCleaned,
                                        createdAt = roomRecord.createdAt,
                                        updatedAt = roomRecord.updatedAt,
                                    )
                                },
                            )
                        }
                    }
                }
            }

            val originalCheckInDate = originalBookingRecord?.checkInDate
            val originalCheckOutDate = originalBookingRecord?.checkOutDate
            val currentTimestamp = OffsetDateTime.now(ZoneOffset.UTC)

            val updatedBookingRecord = originalBookingRecord?.copy(
                guestFirstName = booking.guestFirstName ?: originalBookingRecord.guestFirstName,
                guestLastName = booking.guestLastName ?: originalBookingRecord.guestLastName,
                numOfGuests = booking.numOfGuests ?: originalBookingRecord.numOfGuests,
                occasion = booking.occasion ?: originalBookingRecord.occasion,
                guestNotes = booking.guestNotes ?: originalBookingRecord.guestNotes,
                checkInDate = booking.checkInDate ?: originalBookingRecord.checkInDate,
                checkOutDate = booking.checkOutDate ?: originalBookingRecord.checkOutDate,
                totalCost = if (booking.checkInDate != null && booking.checkOutDate != null &&
                    (booking.checkInDate != originalCheckInDate || booking.checkOutDate != originalCheckOutDate)
                ) {
                    bookingUtils.calculateTotalCostUpdate(booking)
                } else {
                    originalBookingRecord.totalCost
                },

                createdAt = originalBookingRecord.createdAt,
                updatedAt = originalBookingRecord.updatedAt,
                roomDetails = originalBookingRecord.roomDetails,
            )

            val updateQuery = dslContext.update(BOOKING)
                .set(BOOKING.GUEST_FIRST_NAME, updatedBookingRecord?.guestFirstName)
                .set(BOOKING.GUEST_LAST_NAME, updatedBookingRecord?.guestLastName)
                .set(BOOKING.NUM_OF_GUESTS, updatedBookingRecord?.numOfGuests)
                .set(BOOKING.OCCASION, updatedBookingRecord?.occasion)
                .set(BOOKING.GUEST_NOTES, updatedBookingRecord?.guestNotes)
                .set(BOOKING.CHECK_IN_DATE, updatedBookingRecord?.checkInDate)
                .set(BOOKING.CHECK_OUT_DATE, updatedBookingRecord?.checkOutDate)
                .set(BOOKING.TOTAL_COST, updatedBookingRecord?.totalCost)
                .set(BOOKING.UPDATED_AT, currentTimestamp)
                .set(BOOKING.UPDATED_AT, currentTimestamp)
                .set(BOOKING.ROOM_ID, updatedBookingRecord?.roomDetails?.roomId)
                .where(BOOKING.USER_ID.eq(userId))
                .and(BOOKING.BOOKING_ID.eq(bookingId))
                .execute()



            return if (updateQuery == 1) updatedBookingRecord
            else throw RuntimeException("Failed to update booking")

        }catch(e: Exception) {
            throw e
        }
    }
//    fun updateBooking(userId: UUID, bookingId: UUID, booking: UpdateBookingDTO): BookingDTO? {
//        try {
//
//            val bookingRecord = dslContext
//                .select()
//                .from(BOOKING)
//                .where(BOOKING.USER_ID.eq(userId))
//                .and(BOOKING.BOOKING_ID.eq(bookingId))
//                .fetchOne() ?: throw RuntimeException("Booking with ID $bookingId not found")
//
////            val rateRecord = dslContext.select()
////                .from(RATE)
////                .where(RATE.HOTEL_ID.eq(hotelId))
////                .and(DSL.extract(booking.checkInDate, DatePart.MONTH).eq(RATE.MONTH))
////                .fetchOneInto(RateDTO::class.java)
//            val hotelId = bookingRecord.get(BOOKING.HOTEL_ID)
//
//            val durationInDays = ChronoUnit.DAYS.between(booking.checkInDate, booking.checkOutDate)
//            val currentTimestamp = OffsetDateTime.now(ZoneOffset.UTC)
//
////            val totalCost = bookingUtils.calculateTotalCost(booking)
//
//            val originalBookingRecord: BookingDTO? =
//                bookingRecord.get(BOOKING.GUEST_FIRST_NAME)?.let {
//                    bookingRecord.get(BOOKING.NUM_OF_GUESTS)?.let { it1 ->
//                        bookingRecord.get(BOOKING.CHECK_OUT_DATE)?.let { it2 ->
//                            bookingRecord.get(BOOKING.CREATED_AT)?.toLocalDateTime()?.let { it3 ->
//                                BookingDTO(
//                                    bookingId = bookingId,
//                                    userId = userId,
//                                    hotelId = hotelId,
//                                    guestFirstName = it,
//                                    guestLastName = bookingRecord.get(BOOKING.GUEST_LAST_NAME)!!,
//                                    numOfGuests = it1,
//                                    occasion = bookingRecord.get(BOOKING.OCCASION),
//                                    guestNotes = bookingRecord.get(BOOKING.GUEST_NOTES),
//                                    checkInDate = bookingRecord.get(BOOKING.CHECK_IN_DATE)!!,
//                                    checkOutDate = it2,
//                                    totalCost = bookingRecord.get(BOOKING.TOTAL_COST),
//                                    createdAt = it3,
//                                    updatedAt = currentTimestamp.toLocalDateTime(),
//                                    roomDetails = UpdateRoomDTO(
//                                        roomId = bookingRecord.get(BOOKING.ROOM_ID)!!,
//                                        hotelId = bookingRecord.get(BOOKING.HOTEL_ID)!!,
//                                        roomNumber = bookingRecord?.get(BOOKING.ROO),
//                                        roomType = bookingRecord.get(BOOKING.)
//                                    )
//                                )
//                            }
//                        }
//                    }
//                }
//
//            val originalCheckInDate = originalBookingRecord?.checkInDate
//            val originalCheckOutDate = originalBookingRecord?.checkOutDate
//
//
//            val updatedBookingRecord = originalBookingRecord?.copy(
//                guestFirstName = booking.guestFirstName ?: originalBookingRecord.guestFirstName,
//                guestLastName = booking.guestLastName ?: originalBookingRecord.guestLastName,
//                numOfGuests = booking.numOfGuests ?: originalBookingRecord.numOfGuests,
//                occasion = booking.occasion ?: originalBookingRecord.occasion,
//                guestNotes = booking.guestNotes ?: originalBookingRecord.guestNotes,
//                checkInDate = booking.checkInDate ?: originalBookingRecord.checkInDate,
//                checkOutDate = booking.checkOutDate ?: originalBookingRecord.checkOutDate,
////                totalCost = if (booking.checkInDate != null && booking.checkOutDate != null &&
////                    (booking.checkInDate != originalCheckInDate || booking.checkOutDate != originalCheckOutDate)
////                ) {
////                    calculateTotalCost(rateRecord, durationInDays).toBigDecimal()
////                } else {
////                    originalBookingRecord?.totalCost
////                },
//
//                createdAt = originalBookingRecord.createdAt,
//                updatedAt = originalBookingRecord.updatedAt
//            )
//
//
//            val updateQuery = dslContext.update(BOOKING)
//                .set(BOOKING.GUEST_FIRST_NAME, updatedBookingRecord?.guestFirstName)
//                .set(BOOKING.GUEST_LAST_NAME, updatedBookingRecord?.guestLastName)
//                .set(BOOKING.NUM_OF_GUESTS, updatedBookingRecord?.numOfGuests)
//                .set(BOOKING.OCCASION, updatedBookingRecord?.occasion)
//                .set(BOOKING.GUEST_NOTES, updatedBookingRecord?.guestNotes)
//                .set(BOOKING.CHECK_IN_DATE, updatedBookingRecord?.checkInDate)
//                .set(BOOKING.CHECK_OUT_DATE, updatedBookingRecord?.checkOutDate)
//                .set(BOOKING.TOTAL_COST, updatedBookingRecord?.totalCost)
//                .set(BOOKING.UPDATED_AT, currentTimestamp)
//                .set(BOOKING.UPDATED_AT, currentTimestamp)
//                .where(BOOKING.USER_ID.eq(userId))
//                .and(BOOKING.BOOKING_ID.eq(bookingId))
//                .execute()
//
//
//            if (updateQuery == 1) {
//                return updatedBookingRecord
//            } else {
//                throw RuntimeException("Failed to update booking")
//            }
//        } catch (e: Exception) {
//            throw e
//        }
//    }
////

    fun deleteBooking(bookingId: UUID): Boolean {
        try {
            val deleteRecordCount = dslContext.deleteFrom(BOOKING)
                .where(BOOKING.BOOKING_ID.eq(bookingId))
                .execute()

            return deleteRecordCount == 1
        } catch (e: RuntimeException) {
            throw RuntimeException("Failed to delete booking", e)
        } catch (e: Exception) {
            throw e
        }
    }
}