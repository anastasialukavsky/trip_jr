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

    fun fetchUserBookings(id: UUID): MutableList<BookingDTO> {
        try {
            val bookingsRecords = dslContext.select()
                .from(BOOKING)
                .where(BOOKING.USER_ID.eq(id))
                .fetch()

            val bookings: MutableList<BookingDTO> = mutableListOf()

            for (record in bookingsRecords) {
                bookings.add(record.into(BookingDTO::class.java))
            }

            return bookings
        } catch (e: Exception) {
            throw e
        }
    }

    fun fetchUserReviews(id: UUID): MutableList<ReviewDTO> {
        try {
            val reviewsRecords = dslContext.select()
                .from(REVIEW)
                .where(REVIEW.USER_ID.eq(id))
                .fetch()

            val reviews: MutableList<ReviewDTO> = mutableListOf()

            for (record in reviewsRecords) {
                reviews.add(record.into(ReviewDTO::class.java))
            }

            return reviews
        } catch (e: Exception) {
            throw e
        }
    }

}