package com.trip_jr.tripJr.dto.user

import com.trip_jr.tripJr.dto.booking.BookingDTO
import com.trip_jr.tripJr.dto.review.ReviewDTO
import java.util.UUID

data class UserDTO(
    val userId: UUID? = null,
    val email: String = "",
    val firstName: String?,
    val lastName : String?,
    val passwordHash: String = "",
    val bookings: MutableList<BookingDTO>? = mutableListOf(),
    val reviews: MutableList<ReviewDTO>? = mutableListOf()
)

//enum class UserRole(val literal: String) {
//    ADMIN("admin"),
//    USER("user"),
//    OWNER("owner");
//}