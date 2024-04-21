package com.trip_jr.tripJr.dto.hotel

import java.util.UUID

data class LocationDTO(
    val locationId: UUID = UUID.randomUUID(),
    var phoneNumber: String = "",
    var address: String = "",
    var city: String = "",
    var state:String = "",
    var zipCode: String = "",
    var latitude: Double = 0.0,
    val longitude: Double = 0.0,
)