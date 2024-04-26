package com.trip_jr.tripJr.service.review

import com.trip_jr.tripJr.dto.review.ReviewDTO
import com.trip_jr.tripJr.jooq.tables.references.HOTEL
import com.trip_jr.tripJr.jooq.tables.references.REVIEW
import com.trip_jr.tripJr.jooq.tables.references.USERS
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*


@Service
class ReviewService {

    @Autowired
    lateinit var dslContext: DSLContext

    fun createReview(review: ReviewDTO) : ReviewDTO {
        try {
            val reviewRecord = dslContext.insertInto(REVIEW)
                .set(REVIEW.REVIEW_ID, review.reviewId)
                .set(REVIEW.USER_ID, review.userId)
                .set(REVIEW.HOTEL_ID, review.hotelId)
                .set(REVIEW.RATING, review.rating)
                .set(REVIEW.REVIEW_TITLE, review.reviewTitle)
                .set(REVIEW.REVIEW_BODY, review.reviewBody)
                .returningResult(REVIEW.REVIEW_ID)
                .fetchOne()

            if (reviewRecord == null) {
                throw NullPointerException("Failed to create review")
            }

            return review.copy(
                reviewId = reviewRecord.get(REVIEW.REVIEW_ID)
            )
        } catch (e: Exception) {
            throw e
        }
    }
}