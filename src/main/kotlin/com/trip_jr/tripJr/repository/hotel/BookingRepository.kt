package com.trip_jr.tripJr.repository.hotel

import com.trip_jr.tripJr.dto.booking.BookingDTO
import com.trip_jr.tripJr.jooq.tables.references.BOOKING
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime


@Repository
class BookingRepository(private val dslContext: DSLContext) {

    fun findBookingsByCheckOutDate(checkOutDate: LocalDate) : List<BookingDTO>? {
        return dslContext.selectFrom(BOOKING)
            .where(BOOKING.CHECK_OUT_DATE.eq(checkOutDate))
            .fetchInto(BookingDTO::class.java)
    }
}