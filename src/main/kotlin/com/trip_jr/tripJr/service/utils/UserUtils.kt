package com.trip_jr.tripJr.service.utils

import com.trip_jr.tripJr.dto.booking.BookingDTO
import com.trip_jr.tripJr.dto.review.ReviewDTO
import com.trip_jr.tripJr.dto.user.UserDTO
import com.trip_jr.tripJr.jooq.tables.references.BOOKING
import com.trip_jr.tripJr.jooq.tables.references.REVIEW
import com.trip_jr.tripJr.jooq.tables.references.USERS
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserUtils {

    @Autowired
    lateinit var dslContext: DSLContext

    fun getUserById(id: UUID): UserDTO? {
        val userRecord = dslContext.select()
            .from(USERS)
            .where(USERS.USER_ID.eq(id))
            .fetchOne()

        return userRecord?.into(UserDTO::class.java)
    }

    fun fetchUserBookings(id: UUID): List<MutableList<BookingDTO>>? {
        try {
            val bookingsRecord = dslContext.select()
                .from(BOOKING)
                .where(BOOKING.USER_ID.eq(id))
                .fetch()
//                ?: throw NoSuchElementException("Booking records for user with ID $id not found")

            return if (bookingsRecord.isNotEmpty) {
                listOf(bookingsRecord.into(BookingDTO::class.java))

            } else {
                ArrayList()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun fetchUserReviews(id: UUID): List<MutableList<ReviewDTO>>? {
        try {
            val reviewsRecord = dslContext.select()
                .from(REVIEW)
                .where(REVIEW.USER_ID.eq(id))
                .fetch()
//                ?: throw NoSuchElementException("Booking records for user with ID $id not found")

            return if (reviewsRecord.isNotEmpty) {
                listOf(reviewsRecord.into(ReviewDTO::class.java))
            } else {
                ArrayList()
            }
//            return listOf(reviewsRecord.into(ReviewDTO::class.java))
        } catch (e: Exception) {
            throw e
        }
    }
}