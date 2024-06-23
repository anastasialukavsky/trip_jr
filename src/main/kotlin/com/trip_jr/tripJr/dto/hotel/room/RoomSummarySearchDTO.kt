package com.trip_jr.tripJr.dto.hotel.room

import com.trip_jr.tripJr.jooq.enums.BedType
import com.trip_jr.tripJr.jooq.enums.RoomType
import java.util.*

data class RoomSummarySearchDTO(
    val roomId: UUID,
    val roomType: RoomType,
    val bedType: BedType,
    val availability: Boolean
)