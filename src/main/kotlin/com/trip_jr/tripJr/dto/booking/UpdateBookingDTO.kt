package com.trip_jr.tripJr.dto.booking

import com.trip_jr.tripJr.controller.hotel.room.UpdateRoomDTO
import java.time.LocalDate
import java.time.LocalDateTime

data class UpdateBookingDTO(
    val guestFirstName: String?,
    val guestLastName: String?,
    val numOfGuests: Int?,
    val occasion: String?,
    val guestNotes: String?,
    val checkInDate: LocalDate?,
    val checkOutDate: LocalDate?,
    val roomDetails: UpdateRoomDTO,
    val updatedAt: LocalDateTime = LocalDateTime.now()
)