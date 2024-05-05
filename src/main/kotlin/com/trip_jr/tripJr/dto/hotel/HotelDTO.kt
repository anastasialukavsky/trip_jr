package com.trip_jr.tripJr.dto.hotel

import com.trip_jr.tripJr.dto.booking.BookingDTO
import com.trip_jr.tripJr.dto.review.ReviewDTO
import java.time.LocalDateTime
import java.util.UUID

data class HotelDTO(
    val hotelId: UUID? = null,
    val name: String = "",
    val numOfRooms: Int? = 1,
    val description: String? = "",
    var location: LocationDTO,
    val rates: List<RateDTO> = listOf(),
    val amenities: List<AmenityDTO> = listOf(),
    val reviews: List<ReviewDTO> = listOf(),
    val bookings: List<BookingDTO> = listOf(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)