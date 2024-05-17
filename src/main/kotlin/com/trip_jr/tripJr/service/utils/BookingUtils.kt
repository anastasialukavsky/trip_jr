package com.trip_jr.tripJr.service.utils

import com.trip_jr.tripJr.dto.hotel.RoomDTO
import com.trip_jr.tripJr.dto.booking.CreateBookingDTO
import com.trip_jr.tripJr.dto.booking.UpdateBookingDTO
import com.trip_jr.tripJr.dto.hotel.RateDTO
import com.trip_jr.tripJr.jooq.enums.RoomStatus
import com.trip_jr.tripJr.jooq.tables.references.BOOKING
import com.trip_jr.tripJr.jooq.tables.references.RATE
import com.trip_jr.tripJr.jooq.tables.references.ROOM
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.temporal.ChronoUnit
import java.util.*

@Component
class BookingUtils {

    @Autowired
    lateinit var dslContext: DSLContext

    fun isRoomNotBooked(booking: CreateBookingDTO): Boolean {
        try {

        val overlappingBookings = dslContext.selectCount()
            .from(BOOKING)
            .where(BOOKING.ROOM_ID.eq(booking.roomDetails.roomId))
            .and(
                DSL.or(
                    (BOOKING.CHECK_IN_DATE.le(booking.checkInDate).and(BOOKING.CHECK_OUT_DATE.gt(booking.checkInDate))),
                    (BOOKING.CHECK_IN_DATE.lt(booking.checkOutDate)
                        .and(BOOKING.CHECK_OUT_DATE.ge(booking.checkOutDate)))
                )
            )
            .fetchOne(0, Int::class.java)

        return overlappingBookings == 0
        }catch(e: Exception) {
            throw e
        }

    }

    fun changeRoomStatusWhenBooked(booking: CreateBookingDTO): Boolean {
        try {
            if(isRoomNotBooked(booking)) {
                val updateCount = dslContext.update(ROOM)
                    .set(ROOM.ROOM_STATUS, RoomStatus.Occupied)
                    .set(ROOM.AVAILABILITY, false)
                    .where(ROOM.ROOM_ID.eq(booking.roomDetails.roomId))
                    .execute()

                return updateCount > 0
            } else {
                return false
            }
        }catch(e: Exception) {
            throw e
        }
    }


    fun calculateTotalCost(booking: CreateBookingDTO): BigDecimal {
        val durationInDays = ChronoUnit.DAYS.between(booking.checkInDate, booking.checkOutDate)

        val rateRecord = dslContext.select()
            .from(RATE)
            .where(RATE.RATE_ID.eq(booking.roomDetails.rateId))
            .fetchOneInto(RateDTO::class.java) ?: throw NullPointerException("Rate not found")

        val totalCost = rateRecord.rate.times(durationInDays.toDouble()) ?: 0.0
        return totalCost.toBigDecimal()
    }


    fun calculateTotalCostUpdate(booking: UpdateBookingDTO): BigDecimal {
        try {
            val durationInDays = ChronoUnit.DAYS.between(booking.checkInDate, booking.checkOutDate)

            val rateRecord = dslContext.select()
                .from(RATE)
                .where(RATE.RATE_ID.eq(booking.roomDetails.rate?.rateId))
                .fetchOneInto(RateDTO::class.java)

            if (rateRecord != null) {
                val totalCost = rateRecord.rate.times(durationInDays.toDouble()) ?: 0.0
                return totalCost.toBigDecimal()
            } else {
                throw RuntimeException("Rate record not found for rateId: ${booking.roomDetails.rate?.rateId}")
            }
        } catch (e: Exception) {
            throw RuntimeException("Failed to calculate total cost: ${e.message}")
        }
    }


    fun getBookingsRoomById(roomId: UUID) : RoomDTO {
        try {
            val roomRecord = dslContext.select()
                .from(ROOM)
                .where(ROOM.ROOM_ID.eq(roomId))
                .fetchInto(RoomDTO::class.java)

            return roomRecord[0]
        }catch(e: Exception) {
            throw e
        }
    }

}