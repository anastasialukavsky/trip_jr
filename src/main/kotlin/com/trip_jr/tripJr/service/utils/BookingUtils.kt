package com.trip_jr.tripJr.service.utils

import com.trip_jr.tripJr.dto.booking.BookingDTO
import com.trip_jr.tripJr.dto.hotel.RateDTO
import com.trip_jr.tripJr.jooq.tables.references.BOOKING
import com.trip_jr.tripJr.jooq.tables.references.RATE
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.temporal.ChronoUnit

@Component
class BookingUtils {

    @Autowired
    lateinit var dslContext: DSLContext

    fun isRoomAvailable(booking: BookingDTO): Boolean {
        val overlappingBookings = dslContext.selectCount()
            .from(BOOKING)
            .where(BOOKING.ROOM_ID.eq(booking.roomDetails?.roomId))
            .and(
                DSL.or(
                    (BOOKING.CHECK_IN_DATE.le(booking.checkInDate).and(BOOKING.CHECK_OUT_DATE.gt(booking.checkInDate))),
                    (BOOKING.CHECK_IN_DATE.lt(booking.checkOutDate)
                        .and(BOOKING.CHECK_OUT_DATE.ge(booking.checkOutDate)))
                )
            )
            .fetchOne(0, Int::class.java)

        return overlappingBookings == 0

    }

    fun calculateTotalCost(booking: BookingDTO): BigDecimal {
        val durationInDays = ChronoUnit.DAYS.between(booking.checkInDate, booking.checkOutDate)

        val rateRecord = dslContext.select()
            .from(RATE)
            .where(RATE.RATE_ID.eq(booking.roomDetails?.rate?.rateId))
            .fetchOneInto(RateDTO::class.java) ?: throw NullPointerException("Rate not found")

        val totalCost = rateRecord.rate.times(durationInDays.toDouble()) ?: 0.0
        return totalCost.toBigDecimal()
    }

}