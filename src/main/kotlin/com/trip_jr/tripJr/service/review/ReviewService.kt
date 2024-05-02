package com.trip_jr.tripJr.service.review

import com.trip_jr.tripJr.dto.review.ReviewDTO
import com.trip_jr.tripJr.dto.review.UpdateReviewDTO
import com.trip_jr.tripJr.jooq.tables.references.HOTEL
import com.trip_jr.tripJr.jooq.tables.references.REVIEW
import com.trip_jr.tripJr.jooq.tables.references.USERS
import com.trip_jr.tripJr.service.utils.UserUtils
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*


@Service
class ReviewService {

    @Autowired
    lateinit var dslContext: DSLContext

    @Autowired
    lateinit var userUtils: UserUtils

    fun createReview(review: ReviewDTO): ReviewDTO {
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

    fun updateReview(userId: UUID, reviewId: UUID, review: UpdateReviewDTO): ReviewDTO {
        try {
            val userRecord = userUtils.getUserById(userId)

            if (userRecord == null) {
                throw RuntimeException("User record is not found")
            }

            // Fetch the original review record
            val originalReviewRecord = dslContext
                .selectFrom(REVIEW)
                .where(REVIEW.REVIEW_ID.eq(reviewId))
                .fetchOne { record ->
                    record[REVIEW.RATING]?.let {
                        record[REVIEW.REVIEW_TITLE]?.let { it1 ->
                            record[REVIEW.REVIEW_BODY]?.let { it2 ->
                                ReviewDTO(
                                    reviewId = record[REVIEW.REVIEW_ID],
                                    userId = record[REVIEW.USER_ID],
                                    hotelId = record[REVIEW.HOTEL_ID],
                                    rating = it,
                                    reviewTitle = it1,
                                    reviewBody = it2
                                )
                            }
                        }
                    }
                }

            val updatedReviewRecord = originalReviewRecord?.copy()

            review.rating?.let {
                if (updatedReviewRecord != null) {
                    updatedReviewRecord.rating = it
                }
            }
            review.reviewTitle?.let {
                if (updatedReviewRecord != null) {
                    updatedReviewRecord.reviewTitle = it
                }
            }
            review.reviewBody?.let {
                if (updatedReviewRecord != null) {
                    updatedReviewRecord.reviewBody = it
                }
            }

            val updateQuery = dslContext.update(REVIEW)
                .set(REVIEW.REVIEW_TITLE, updatedReviewRecord?.reviewTitle)
                .set(REVIEW.REVIEW_BODY, updatedReviewRecord?.reviewBody)
                .where(REVIEW.REVIEW_ID.eq(reviewId))
                .execute()

            if (updateQuery == 1) {
                val updatedRecord = dslContext
                    .selectFrom(REVIEW)
                    .where(REVIEW.REVIEW_ID.eq(reviewId))
                    .fetchOneInto(ReviewDTO::class.java)

                updatedRecord?.let {
                    if (originalReviewRecord != null) {
                        updatedRecord.reviewId = originalReviewRecord.reviewId
                    }
                    if (originalReviewRecord != null) {
                        updatedRecord.userId = originalReviewRecord.userId
                    }
                    if (originalReviewRecord != null) {
                        updatedRecord.hotelId = originalReviewRecord.hotelId
                    }
                }

                return updatedRecord ?: throw RuntimeException("Failed to fetch updated review")
            } else {
                throw RuntimeException("Failed to update review")
            }
        } catch (e: Exception) {
            throw e // You can handle exceptions more gracefully here
        }
    }


}