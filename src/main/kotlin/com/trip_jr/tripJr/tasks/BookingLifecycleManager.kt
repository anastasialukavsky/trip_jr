package com.trip_jr.tripJr.tasks

import com.trip_jr.tripJr.jooq.enums.RoomStatus
import com.trip_jr.tripJr.repository.hotel.BookingRepository
import com.trip_jr.tripJr.repository.hotel.room.RoomRepository
import org.slf4j.LoggerFactory
//import org.hibernate.validator.internal.util.logging.LoggerFactory
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

    private val logger = LoggerFactory.getLogger(BookingLifecycleManager::class.java)

//    @Scheduled(fixedRate = 6000) //checks db for expired bookings every 24hrs at 11am EST every day
    @Scheduled(cron = "0 0 11 * * ?", zone = "America/New_York") //checks db for expired bookings every 24hrs at 11am EST every day
    fun removeExpiredBookings() {
        logger.info("Running scheduled task to check for expired bookings")
        val currentDate = LocalDate.now()

        val expiredBookings = bookingRepository.findBookingsByCheckOutDate(currentDate)
        if (expiredBookings != null && expiredBookings.isNotEmpty()) {

            logger.info("FETCHED ${expiredBookings?.size} bookings")
        } else {
            logger.warn("No expired bookings found")
        }

        if (expiredBookings != null) {
            for (booking in expiredBookings) {
                logger.info("Processing Booking $booking")
                val roomId = booking.roomDetails?.roomId
                if (roomId == null) {
                    logger.warn("Room $roomId does not exist")
                    continue
                }

                roomRepository.updateRoomStatus(roomId, RoomStatus.Maintenance)
                logger.info("Booking $roomId has been expired, MAINTENANCE is scheduled")
                scheduleMaintenanceEnd(roomId)
            }
        }
    }


    private fun scheduleMaintenanceEnd(roomId: UUID) {
        val executorService = Executors.newSingleThreadScheduledExecutor()

        executorService.schedule({
            roomRepository.updateRoomStatus(roomId, RoomStatus.Vacant)
            logger.info("Updated room {} to Vacant", roomId)
        }, 1, TimeUnit.HOURS)
    }

}