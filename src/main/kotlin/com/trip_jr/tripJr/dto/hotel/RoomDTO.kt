package com.trip_jr.tripJr.dto.hotel

import com.trip_jr.tripJr.jooq.enums.BedType
import com.trip_jr.tripJr.jooq.enums.RoomStatus
import com.trip_jr.tripJr.jooq.enums.RoomType
import java.time.LocalDateTime
import java.util.*

data class RoomDTO(
    val roomId: UUID? = null,
    val hotelId: UUID,
    val rate: RateDTO? = null,
    val roomNumber: Int,
    val roomType: RoomType,
    val roomStatus: RoomStatus,
    val bedType: BedType,
    val maximumOccupancy: Int,
    val description: String,
    val floor: Int,
    val availability: Boolean,
    val lastCleaned: LocalDateTime? = LocalDateTime.now(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    )