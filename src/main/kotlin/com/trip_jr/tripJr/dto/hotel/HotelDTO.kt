package com.trip_jr.tripJr.dto.hotel

import com.trip_jr.tripJr.dto.booking.BookingDTO
import com.trip_jr.tripJr.dto.review.ReviewDTO
import java.util.UUID

data class HotelDTO(
    val hotelId: UUID? = null,
    val name: String = "",
    var location: LocationDTO,
    val rates: List<RateDTO> = listOf(),
    val amenities: List<AmenityDTO> = listOf(),
    val reviews: List<ReviewDTO> = listOf(),
    val bookings: List<BookingDTO> = listOf()
)