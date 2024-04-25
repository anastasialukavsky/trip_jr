package com.trip_jr.tripJr.dto.user

import java.util.UUID

data class UserDTO(
    val userId: UUID,
    val email: String,
    val firstName: String,
    val lastName : String,
    val passwordHash: String,
    val role:UserRole = UserRole.USER,

)

enum class UserRole {
    ADMIN,
    USER,
    OWNER
}