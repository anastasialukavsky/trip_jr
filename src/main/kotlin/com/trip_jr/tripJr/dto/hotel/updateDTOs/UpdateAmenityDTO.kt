package com.trip_jr.tripJr.dto.hotel.updateDTOs

import java.time.LocalDateTime

data class UpdateAmenityDTO(
    val amenityName: String?,
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)