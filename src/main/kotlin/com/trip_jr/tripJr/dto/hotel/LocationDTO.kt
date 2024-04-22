package com.trip_jr.tripJr.dto.hotel

import java.util.UUID

data class LocationDTO(
    val locationId: UUID? =null,
    var phoneNumber: String = "",
    var address: String = "",
    var city: String = "",
    var state:String = "",
    var zip: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
)