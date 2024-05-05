package com.trip_jr.tripJr.dto.hotel.updateDTOs

import java.util.*

data class UpdateLocationDTO(
    var phoneNumber: String?,
    var address: String?,
    var city: String?,
    var state: String?,
    var zip: String?,
    var latitude: Double?,
    var longitude: Double?,
)