package com.trip_jr.tripJr.service.utils

import com.trip_jr.tripJr.dto.hotel.RateDTO
import com.trip_jr.tripJr.jooq.tables.references.RATE
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class RateUtils {


    @Autowired
    lateinit var dslContext: DSLContext

    fun fetchRateDto(rateId: UUID?): RateDTO? {
        if (rateId == null) return null

        val rateRecord = dslContext.selectFrom(RATE)
            .where(RATE.RATE_ID.eq(rateId))
            .fetchOne()

        return rateRecord?.let {
            it[RATE.RATE_]?.let { it1 ->
                it[RATE.MONTH]?.let { it2 ->
                    it[RATE.DEFAULT_RATE]?.let { it3 ->
                        RateDTO(
                            rateId = it[RATE.RATE_ID],
                            rate = it1,
                            month = it2,
                            hotelId = it[RATE.HOTEL_ID],
                            defaultRate = it3
                        )
                    }
                }
            }
        }
    }

}