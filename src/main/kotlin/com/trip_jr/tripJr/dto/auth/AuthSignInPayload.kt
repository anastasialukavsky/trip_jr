package com.trip_jr.tripJr.dto.auth

data class AuthSignInPayload(
    val token: String,
    val email: String
)