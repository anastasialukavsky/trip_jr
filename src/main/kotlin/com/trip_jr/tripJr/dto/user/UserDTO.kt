package com.trip_jr.tripJr.dto.user

import java.util.UUID

data class UserDTO(
    val userId: UUID? = null,
    val email: String = "",
    val firstName: String?,
    val lastName : String?,
    val passwordHash: String = "",
)

//enum class UserRole(val literal: String) {
//    ADMIN("admin"),
//    USER("user"),
//    OWNER("owner");
//}