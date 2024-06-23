package com.trip_jr.tripJr.dto.hotel

import com.trip_jr.tripJr.dto.amenities.AmenitySummarySearchDTO
import com.trip_jr.tripJr.dto.hotel.room.RoomSummarySearchDTO
import com.trip_jr.tripJr.dto.location.LocationSummarySearchDTO
import java.util.*

data class HotelSearchDTO(
    val hotelId: UUID,
    val name: String,
    val location: LocationSummarySearchDTO,
    val amenities: List<AmenitySummarySearchDTO>,
    val roomSummary: RoomSummarySearchDTO,
    val rate: RateDTO
)