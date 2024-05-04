package com.trip_jr.tripJr.service.booking

import com.trip_jr.tripJr.dto.booking.BookingDTO
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
            val bookingRecord = dslContext.insertInto(BOOKING)
                .set(BOOKING.BOOKING_ID, bookingId)
                .set(BOOKING.USER_ID, booking.userId)
                .set(BOOKING.HOTEL_ID, booking.hotelId)
                .set(BOOKING.CHECK_IN_DATE, booking.checkInDate)
                .set(BOOKING.CHECK_OUT_DATE, booking.checkOutDate)
                .set(BOOKING.TOTAL_COST, totalCostBigDecimal)
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

    fun updateBooking(userId: UUID, bookingId: UUID, hotelId: UUID,  booking: BookingDTO): BookingDTO {
        try {

            val bookingRecord = dslContext
                .select()
                .from(BOOKING)
                .where(BOOKING.USER_ID.eq(userId))
                .and(BOOKING.BOOKING_ID.eq(bookingId))
                .fetchOne()
            if (bookingRecord == null) {
                throw RuntimeException("Booking not found")
            }

            //maybe dont need this
            val bookingMapping = BookingDTO(
                bookingId = bookingId,
                userId = userId,
                hotelId = hotelId,
                checkInDate = booking.checkInDate,
                checkOutDate = booking.checkOutDate,
                totalCost = booking.totalCost,
            )


            val updatedBookingRecord = dslContext.update(BOOKING)
                .set(BOOKING.CHECK_IN_DATE, booking.checkInDate)
                .set(BOOKING.CHECK_OUT_DATE, booking.checkOutDate)
                .where(BOOKING.USER_ID.eq(userId))
                .and(BOOKING.BOOKING_ID.eq(bookingId))
                .execute()

            if (updatedBookingRecord == 1) {
                val updatedRecord = dslContext
                    .selectFrom(BOOKING)
                    .where(BOOKING.USER_ID.eq(userId))
                    .and(BOOKING.BOOKING_ID.eq(bookingId))
                    .fetchOneInto(BookingDTO::class.java)

                return updatedRecord ?: throw RuntimeException("Failed to fetch updated booking")
            } else {
                throw RuntimeException("Failed to update booking")
            }
        } catch (e: Exception) {
            throw e
        }
    }


}