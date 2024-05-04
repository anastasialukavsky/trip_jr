package com.trip_jr.tripJr.service.booking

import com.trip_jr.tripJr.dto.booking.BookingDTO
import com.trip_jr.tripJr.dto.booking.UpdateBookingDTO
import com.trip_jr.tripJr.dto.hotel.RateDTO
import com.trip_jr.tripJr.jooq.tables.references.BOOKING
import com.trip_jr.tripJr.jooq.tables.references.RATE
import com.trip_jr.tripJr.service.hotel.HotelService
import com.trip_jr.tripJr.service.utils.UUIDUtils
import org.jooq.DSLContext
import org.jooq.DatePart
import org.jooq.impl.DSL
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDateTime
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

    private val logger = LoggerFactory.getLogger(HotelService::class.java)

    fun getAllBookingsByUserId(userId: UUID): List<BookingDTO> {
        try {
            val bookingsRecord = dslContext
                .select()
                .from(BOOKING)
                .where(BOOKING.USER_ID.eq(userId))
                .fetch()

            return bookingsRecord.into(BookingDTO::class.java)
        } catch (e: Exception) {
            throw e
        }
    }

    fun getSingleBookingByUserId(userId: UUID, bookingId: UUID): BookingDTO? {
        try {
            val bookingRecord = dslContext
                .select()
                .from(BOOKING)
                .where(BOOKING.USER_ID.eq(userId))
                .and(BOOKING.BOOKING_ID.eq(bookingId))
                .fetchOne()

            return bookingRecord?.into(BookingDTO::class.java)
        } catch (e: Exception) {
            throw e
        }
    }


    fun createBooking(booking: BookingDTO): BookingDTO {
        try {
            val durationInDays = ChronoUnit.DAYS.between(booking.checkInDate, booking.checkOutDate)


            val rateRecord = dslContext.select()
                .from(RATE)
                .where(RATE.HOTEL_ID.eq(booking.hotelId))
                .and(DSL.extract(booking.checkInDate, DatePart.MONTH).eq(RATE.MONTH))
                .fetchOneInto(RateDTO::class.java)

            val totalCost = rateRecord?.rate?.times(durationInDays.toDouble()) ?: 0.0
            val totalCostBigDecimal = totalCost.toBigDecimal()
            val bookingId = booking.bookingId ?: uuidUtils.generateUUID()

            val currentTimestamp = OffsetDateTime.now(ZoneOffset.UTC)

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
                .set(BOOKING.TOTAL_COST, totalCostBigDecimal)
                .set(BOOKING.CREATED_AT, currentTimestamp)
                .set(BOOKING.UPDATED_AT, currentTimestamp)
                .returningResult(BOOKING.BOOKING_ID)
                .fetchOne()

            bookingRecord ?: throw NullPointerException("Failed to create booking record!")

            val createdBooking = booking.copy(
                bookingId = bookingRecord.get(BOOKING.BOOKING_ID),
                totalCost = totalCostBigDecimal
            )

            return createdBooking
        } catch (e: Exception) {
            throw e
        }
    }

    fun calculateTotalCost(rateRecord: RateDTO?, durationInDays: Long): Double {
        val rate = rateRecord?.rate ?: 0.0
        return rate * durationInDays
    }

    fun updateBooking(userId: UUID, bookingId: UUID, hotelId: UUID, booking: UpdateBookingDTO): BookingDTO? {
        try {

            val bookingRecord = dslContext
                .select()
                .from(BOOKING)
                .where(BOOKING.USER_ID.eq(userId))
                .and(BOOKING.BOOKING_ID.eq(bookingId))
                .fetchOne() ?: throw RuntimeException("Booking with ID $bookingId not found")

            val rateRecord = dslContext.select()
                .from(RATE)
                .where(RATE.HOTEL_ID.eq(hotelId))
                .and(DSL.extract(booking.checkInDate, DatePart.MONTH).eq(RATE.MONTH))
                .fetchOneInto(RateDTO::class.java)

            val durationInDays = ChronoUnit.DAYS.between(booking.checkInDate, booking.checkOutDate)
            val currentTimestamp = OffsetDateTime.now(ZoneOffset.UTC)

            val originalBookingRecord: BookingDTO? =
                bookingRecord.get(BOOKING.GUEST_FIRST_NAME)?.let {
                    bookingRecord.get(BOOKING.NUM_OF_GUESTS)?.let { it1 ->
                        bookingRecord.get(BOOKING.CHECK_OUT_DATE)?.let { it2 ->
                            bookingRecord.get(BOOKING.CREATED_AT)?.toLocalDateTime()?.let { it3 ->
                                BookingDTO(
                                    bookingId = bookingId,
                                    userId = userId,
                                    hotelId = hotelId,
                                    guestFirstName = it,
                                    guestLastName = bookingRecord.get(BOOKING.GUEST_LAST_NAME)!!,
                                    numOfGuests = it1,
                                    occasion = bookingRecord.get(BOOKING.OCCASION),
                                    guestNotes = bookingRecord.get(BOOKING.GUEST_NOTES),
                                    checkInDate = bookingRecord.get(BOOKING.CHECK_IN_DATE)!!,
                                    checkOutDate = it2,
                                    totalCost = bookingRecord.get(BOOKING.TOTAL_COST),
                                    createdAt = it3,
                                    updatedAt = currentTimestamp.toLocalDateTime()
                                )
                            }
                        }
                    }
                }

            val originalCheckInDate = originalBookingRecord?.checkInDate
            val originalCheckOutDate = originalBookingRecord?.checkOutDate


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
                    calculateTotalCost(rateRecord, durationInDays).toBigDecimal()
                } else {
                    originalBookingRecord?.totalCost
                },

                createdAt = originalBookingRecord.createdAt,
                updatedAt = originalBookingRecord.updatedAt
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
                .where(BOOKING.USER_ID.eq(userId))
                .and(BOOKING.BOOKING_ID.eq(bookingId))
                .execute()


            if (updateQuery == 1) {
                return updatedBookingRecord
            } else {
                throw RuntimeException("Failed to update booking")
            }
        } catch (e: Exception) {
            throw e
        }
    }

}