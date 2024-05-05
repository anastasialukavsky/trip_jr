package com.trip_jr.tripJr.dto.hotel

import java.time.LocalDateTime
import java.util.UUID

data class AmenityDTO(
    val amenityId: UUID? = null,
    var amenityName: String = "",
    val hotelId: UUID?,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)