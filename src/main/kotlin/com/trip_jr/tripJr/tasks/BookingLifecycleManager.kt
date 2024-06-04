package com.trip_jr.tripJr.tasks

import com.trip_jr.tripJr.jooq.enums.RoomStatus
import com.trip_jr.tripJr.repository.hotel.BookingRepository
import com.trip_jr.tripJr.repository.hotel.room.RoomRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Component
class BookingLifecycleManager(
    private val bookingRepository: BookingRepository,
    private val roomRepository: RoomRepository
) {


    @Scheduled(cron = "0 0 11 * * ?", zone = "America/New_York") //checks db for expired bookings every 24hrs at 11am EST every day
    fun removeExpiredBookings() {
        val currentDate = LocalDate.now()

        val expiredBookings = bookingRepository.findBookingsByCheckOutDate(currentDate)

        if (expiredBookings != null) {
            for (booking in expiredBookings) {
                val roomId = booking.roomDetails?.roomId ?: continue

                roomRepository.updateRoomStatus(roomId, RoomStatus.Maintenance)
                scheduleMaintenanceEnd(roomId)
            }
        }


    }


    private fun scheduleMaintenanceEnd(roomId: UUID) {
        val executorService = Executors.newSingleThreadScheduledExecutor()

        executorService.schedule({
            roomRepository.updateRoomStatus(roomId, RoomStatus.Vacant)
        }, 1, TimeUnit.HOURS)
    }

}