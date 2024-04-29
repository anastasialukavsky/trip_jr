package com.trip_jr.tripJr.dto.booking

import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

data class BookingDTO(
    val bookingId: UUID? = null,
    val userId: UUID?,
    val hotelId: UUID?,
    val checkInDate: LocalDate,
    val checkOutDate: LocalDate,
    val totalCost: BigDecimal,
)