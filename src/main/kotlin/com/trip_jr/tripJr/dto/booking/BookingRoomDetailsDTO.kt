package com.trip_jr.tripJr.dto.booking

import java.util.UUID


data class BookingRoomDetailsDTO(
    val roomId: UUID,
    val rateId: UUID
)