package com.trip_jr.tripJr.service.hotel

import com.trip_jr.tripJr.jooq.tables.Hotel
import com.trip_jr.tripJr.jooq.tables.references.RATE
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class RateService {

    @Autowired
    lateinit var dslContext: DSLContext

    fun deleteRate(id: UUID): Boolean {
        try {
            val deleteRecordCount = dslContext.deleteFrom(RATE)
                .where(RATE.RATE_ID.eq(id))
                .execute()

            return deleteRecordCount == 1
        } catch (e: RuntimeException) {
            throw RuntimeException("Failed to delete rate with ID $id", e)
        } catch (e: Exception) {
            throw e
        }
    }
}