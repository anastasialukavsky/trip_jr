package com.trip_jr.tripJr.dto.review

import java.util.*

data class ReviewDTO(
    var reviewId: UUID? = null,
    var userId: UUID?,
    var hotelId: UUID?,
    var rating: Int,
    var reviewTitle: String,
    var reviewBody: String,
    )

data class UpdateReviewDTO(
    val rating: Int?,
    val reviewTitle: String?,
    val reviewBody: String?,
)