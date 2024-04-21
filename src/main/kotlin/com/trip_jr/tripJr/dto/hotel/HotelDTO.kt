package com.trip_jr.tripJr.dto.hotel

import java.util.UUID

data class HotelDTO(
    val hotelId: UUID = UUID.randomUUID(),
    val name: String = "",
    var location: LocationDTO? = null
)