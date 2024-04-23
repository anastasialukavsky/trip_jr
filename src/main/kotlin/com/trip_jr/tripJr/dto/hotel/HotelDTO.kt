package com.trip_jr.tripJr.dto.hotel

import java.util.UUID

data class HotelDTO(
    val hotelId: UUID? = null,
    val name: String = "",
    var location: LocationDTO,
    val rates: List<RateDTO> = listOf(),
    val amenities: List<AmenityDTO> = listOf()
)