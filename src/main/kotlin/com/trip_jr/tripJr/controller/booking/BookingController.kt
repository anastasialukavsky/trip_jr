package com.trip_jr.tripJr.controller.booking

import com.trip_jr.tripJr.dto.booking.BookingDTO
import com.trip_jr.tripJr.service.booking.BookingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Controller
class BookingController {

    @Autowired
    lateinit var bookingService: BookingService

    @MutationMapping(name= "createBooking")
    fun createBooking(@Argument(name="booking") booking: BookingDTO): BookingDTO {
        val bookingId = UUID.randomUUID()

        val newBooking = BookingDTO(
            bookingId = bookingId,
            userId = booking.userId,
            hotelId = booking.hotelId,
            checkInDate = booking.checkInDate,
            checkOutDate = booking.checkOutDate,
            totalCost = booking.totalCost
        )

        return bookingService.createBooking(newBooking)
    }
}