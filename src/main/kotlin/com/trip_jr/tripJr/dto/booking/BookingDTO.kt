package com.trip_jr.tripJr.dto.booking

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class BookingDTO(
    val bookingId: UUID? = null,
    val userId: UUID?,
    val hotelId: UUID?,
    val guestFirstName: String,
    val guestLastName: String,
    val numOfGuests: Int,
    val occasion: String? = "",
    val guestNotes: String? = "",
    val checkInDate: LocalDate,
    val checkOutDate: LocalDate,
    val totalCost: BigDecimal?,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)