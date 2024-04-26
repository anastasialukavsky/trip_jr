package com.trip_jr.tripJr.dto.review

import java.util.*

data class ReviewDTO(
    val reviewId: UUID? = null,
    val userId: UUID?,
    val hotelId: UUID?,
    val rating: Int,
    val reviewTitle: String,
    val reviewBody: String,
    )