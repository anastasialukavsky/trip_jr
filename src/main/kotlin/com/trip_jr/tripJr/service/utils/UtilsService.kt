package com.trip_jr.tripJr.service.utils

import com.trip_jr.tripJr.jooq.tables.references.HOTEL
import com.trip_jr.tripJr.jooq.tables.references.USERS
import org.jooq.DSLContext
import org.jooq.exception.DataAccessException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*


@Service
class UtilsService {

    @Autowired
    lateinit var dslContext: DSLContext


    fun checkUserExists(userId: UUID): Boolean {
        return try {
            val count = dslContext.selectCount()
                .from(USERS)
                .where(USERS.USER_ID.eq(userId))
                .fetchOne(0, Int::class.java)

            count!! > 0
        } catch (e: DataAccessException) {
            false
        }
    }

    fun checkHotelExists(hotelId: UUID): Boolean {
        return try {
            val count = dslContext.selectCount()
                .from(HOTEL)
                .where(HOTEL.HOTEL_ID.eq(hotelId))
                .fetchOne(0, Int::class.java)

            count!! > 0
        } catch (e: DataAccessException) {
            false
        }
    }
}