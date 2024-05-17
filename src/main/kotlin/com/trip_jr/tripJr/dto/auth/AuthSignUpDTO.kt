package com.trip_jr.tripJr.dto.auth

data class AuthSignUpDTO(
    val email: String,
    val firstName: String,
    val lastName : String,
    val password: String
)