package com.trip_jr.tripJr.service.utils

import com.trip_jr.tripJr.dto.booking.BookingDTO
import com.trip_jr.tripJr.dto.hotel.AmenityDTO
import com.trip_jr.tripJr.dto.hotel.RateDTO
import com.trip_jr.tripJr.dto.review.ReviewDTO
import com.trip_jr.tripJr.jooq.tables.references.*
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*


@Component
class HotelByIdUtils {

    @Autowired
    lateinit var dslContext: DSLContext


    fun getHotelAmenities(hotelId: UUID): MutableList<AmenityDTO> {
        try {
            val amenitiesRecord = dslContext
                .select()
                .from(AMENITY)
                .where(AMENITY.HOTEL_ID.eq(hotelId))
                .fetchInto(AmenityDTO::class.java)

            return amenitiesRecord
        } catch (e: Exception) {
            throw e;
        }
    }

    fun getHotelReviews(hotelId: UUID): MutableList<ReviewDTO> {
        try {
            val reviewsRecord = dslContext.select()
                .from(REVIEW)
                .where(REVIEW.HOTEL_ID.eq(hotelId))
                .fetchInto(ReviewDTO::class.java)

            return reviewsRecord
        } catch (e: Exception) {
            throw e;
        }
    }

    fun getHotelBookings(hotelId: UUID): MutableList<BookingDTO> {
        try {
            val bookingsRecord = dslContext.select()
                .from(BOOKING)
                .join(ROOM).on(BOOKING.HOTEL_ID.eq(ROOM.HOTEL_ID))
                .where(BOOKING.HOTEL_ID.eq(hotelId))
                .fetchInto(BookingDTO::class.java)

            return bookingsRecord
        }catch(e: Exception) {
            throw e
        }
    }
}