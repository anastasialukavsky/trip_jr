package com.trip_jr.tripJr.dto.user

import com.trip_jr.tripJr.jooq.enums.UserRole
import java.util.*

data class UserTokenDTO(
    val userId: UUID,
    val email: String,
    val passwordHash:String,
    val role: UserRole,
)