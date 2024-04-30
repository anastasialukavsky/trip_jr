package com.trip_jr.tripJr.controller.booking

import com.trip_jr.tripJr.dto.booking.BookingDTO
import com.trip_jr.tripJr.service.booking.BookingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class BookingController {

    @Autowired
    lateinit var bookingService: BookingService

    @QueryMapping(name="bookingsByUserId")
    fun bookingsByUserId(@Argument userId: UUID): List<BookingDTO> {
        return bookingService.getAllBookingsByUserId(userId)
    }

    @MutationMapping(name= "createBooking")
    fun createBooking(@Argument(name="booking") booking: BookingDTO): BookingDTO {
        return bookingService.createBooking(booking)
    }
}