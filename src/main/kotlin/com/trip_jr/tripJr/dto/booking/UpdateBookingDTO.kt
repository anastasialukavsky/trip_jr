package com.trip_jr.tripJr.dto.booking

import java.time.LocalDate
import java.time.LocalDateTime

data class UpdateBookingDTO(
    val guestFirstName: String?,
    val guestLastName: String?,
    val numOfGuests: Int?,
    val occasion: String?,
    val guestNotes: String?,
    val checkInDate: LocalDate?,
    val checkOutDate: LocalDate?,
    val updatedAt: LocalDateTime = LocalDateTime.now()
)