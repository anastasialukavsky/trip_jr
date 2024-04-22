package com.trip_jr.tripJr.dto.hotel

import java.util.UUID

data class RateDTO(
    val rateId: UUID? = null,
    val hotelId: UUID?,
    val rate: Double,
    val month: Int,
    val defaultRate:Double
)