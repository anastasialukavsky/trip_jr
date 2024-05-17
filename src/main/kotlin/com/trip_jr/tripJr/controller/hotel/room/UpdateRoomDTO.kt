package com.trip_jr.tripJr.controller.hotel.room

import com.trip_jr.tripJr.dto.hotel.RateDTO
import com.trip_jr.tripJr.jooq.enums.BedType
import com.trip_jr.tripJr.jooq.enums.RoomStatus
import com.trip_jr.tripJr.jooq.enums.RoomType
import java.time.LocalDateTime
import java.util.*

data class UpdateRoomDTO(
    val roomId: UUID?,
    val hotelId: UUID?,
    val roomNumber: Int?,
    val roomType: RoomType?,
    val roomStatus: RoomStatus?,
    val bedType: BedType?,
    val maximumOccupancy: Int?,
    val description: String?,
    val floor: Int?,
    val rate: RateDTO?,
    val availability: Boolean?,
    val lastCleaned: LocalDateTime? = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = LocalDateTime.now(),
)