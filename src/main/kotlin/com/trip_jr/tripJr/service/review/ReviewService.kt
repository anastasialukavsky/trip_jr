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

    fun updateReview(userId: UUID, reviewId: UUID, review: UpdateReviewDTO): ReviewDTO? {
        try {


            val record = dslContext
                .select()
                .from(REVIEW)
                .where(REVIEW.REVIEW_ID.eq(reviewId))
                .fetchOne()


            val originalReviewRecord: ReviewDTO =
                ReviewDTO(
                    reviewId = reviewId,
                    userId = userId,
                    hotelId = record?.get(REVIEW.HOTEL_ID),
                    rating = record?.get(REVIEW.RATING)!!,
                    reviewTitle = record.get(REVIEW.REVIEW_TITLE)!!,
                    reviewBody = record.get(REVIEW.REVIEW_BODY)!!
                )


            val updatedReview = originalReviewRecord?.copy(
                rating = review.rating ?: originalReviewRecord.rating,
                reviewTitle = review.reviewTitle ?: originalReviewRecord.reviewTitle,
                reviewBody = review.reviewBody ?: originalReviewRecord.reviewBody
            )


            val updateQuery = dslContext.update(REVIEW)
                .set(REVIEW.RATING, updatedReview?.rating)
                .set(REVIEW.REVIEW_TITLE, updatedReview?.reviewTitle)
                .set(REVIEW.REVIEW_BODY, updatedReview?.reviewBody)
                .where(REVIEW.REVIEW_ID.eq(reviewId))
                .execute()

            return if (updateQuery == 1) updatedReview
             else throw RuntimeException("Failed to update review")


        } catch (e: Exception) {
            throw e
        }
    }

}