package com.trip_jr.tripJr.dto.user

import com.trip_jr.tripJr.dto.booking.BookingDTO
import com.trip_jr.tripJr.dto.review.ReviewDTO
import java.time.LocalDateTime
import java.util.UUID

data class UserDTO(
    val userId: UUID? = null,
    val email: String = "",
    val firstName: String?,
    val lastName : String?,
    val passwordHash: String = "",
    val bookings: MutableList<BookingDTO>? = mutableListOf(),
    val reviews: MutableList<ReviewDTO>? = mutableListOf(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)