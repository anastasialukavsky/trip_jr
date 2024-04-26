package com.trip_jr.tripJr.dto.booking

import java.util.*

data class BookingDTO(
    val bookingId: UUID? = null,
    val userId: UUID?,
    val hotelId: UUID?,
    val checkInDate: Date,
    val checkOutDate: Date,
    val totalCost: Double,
)