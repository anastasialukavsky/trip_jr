package com.trip_jr.tripJr.service.booking

import com.trip_jr.tripJr.dto.booking.BookingDTO
import com.trip_jr.tripJr.jooq.tables.records.BookingRecord
import com.trip_jr.tripJr.jooq.tables.references.BOOKING
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Service
class BookingService {

    @Autowired
    lateinit var dslContext: DSLContext


    fun createBooking(booking: BookingDTO): BookingDTO {
        try {
            val totalCostBigDecimal = BigDecimal.valueOf(booking.totalCost)

            val bookingRecord = dslContext.insertInto(BOOKING)
                .set(BOOKING.BOOKING_ID, booking.bookingId)
                .set(BOOKING.USER_ID, booking.userId)
                .set(BOOKING.HOTEL_ID, booking.hotelId)
                .set(BOOKING.CHECK_IN_DATE, booking.checkInDate)
                .set(BOOKING.CHECK_OUT_DATE, booking.checkOutDate)
                .set(BOOKING.TOTAL_COST, totalCostBigDecimal)
                .returningResult(BOOKING.BOOKING_ID)
                .fetchOne()

            bookingRecord ?: throw NullPointerException("Failed to create booking record!")



            return booking.copy(bookingId = bookingRecord.get(BOOKING.BOOKING_ID))


        } catch (e: Exception) {
            throw e
        }
    }
}