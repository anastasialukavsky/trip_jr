package com.trip_jr.tripJr.dto.hotel.updateDTOs


import java.time.LocalDateTime

data class UpdateHotelDTO(
    val name: String?,
    val numOfRooms: Int?,
    val description: String?,
//    var location: LocationDTO?,
//    val rates: List<RateDTO?>?,
//    val amenities: List<AmenityDTO?>?,
//    val reviews: List<ReviewDTO?>?,
//    val bookings: List<BookingDTO?>?,
    val updatedAt: LocalDateTime = LocalDateTime.now()
)