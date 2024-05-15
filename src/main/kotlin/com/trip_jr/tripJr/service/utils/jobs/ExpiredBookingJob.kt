package com.trip_jr.tripJr.service.utils.jobs

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component


@Component
class ExpiredBookingJob {
    @Scheduled(cron = "0 0 1 * * ?")
    fun checkExpiredBookingsAndChangeRoomStatus() {
//        val expiredBookings = bookingService.getExpiredBookings()

//        for (booking in expiredBookings) {
//            bookingService.updateRoomStatusToVacant(booking.roomDetails.roomId)
    }
}