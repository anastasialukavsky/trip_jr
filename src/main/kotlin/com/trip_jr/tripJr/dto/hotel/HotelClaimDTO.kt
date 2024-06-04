package com.trip_jr.tripJr.dto.hotel

import com.trip_jr.tripJr.jooq.enums.ClaimStatus
import java.time.LocalDateTime
import java.util.UUID

data class HotelClaimDTO(
    val claimId: UUID,
    val hotelId: UUID? = null,
    val userId: UUID? =null,
    val status: ClaimStatus,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)