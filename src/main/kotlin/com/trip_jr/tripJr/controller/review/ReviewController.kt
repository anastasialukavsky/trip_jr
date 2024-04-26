package com.trip_jr.tripJr.controller.review

import com.trip_jr.tripJr.dto.review.ReviewDTO
import com.trip_jr.tripJr.service.review.ReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller
import java.util.*


@Controller
class ReviewController {

    @Autowired
    lateinit var reviewService: ReviewService


    @MutationMapping(name = "createReview")
    fun createReview(@Argument(name = "review") review: ReviewDTO): ReviewDTO {
        val reviewId = UUID.randomUUID()
        val newReview = ReviewDTO(
            reviewId = reviewId,
            userId = review.userId,
            hotelId = review.hotelId,
            rating = review.rating,
            reviewTitle = review.reviewTitle,
            reviewBody = review.reviewBody
        )
        return reviewService.createReview(newReview)
    }
}