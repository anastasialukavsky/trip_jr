package com.trip_jr.tripJr.repository.hotel

import com.trip_jr.tripJr.dto.hotel.HotelClaimDTO
import com.trip_jr.tripJr.jooq.tables.references.HOTEL
import com.trip_jr.tripJr.jooq.tables.references.HOTEL_CLAIM
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class HotelClaimRepository(private val dslContext: DSLContext) {


    fun saveHotelClaim(claim: HotelClaimDTO): HotelClaimDTO {
        dslContext.insertInto(HOTEL_CLAIM)
            .set(HOTEL_CLAIM.CLAIM_ID, claim.claimId)
            .set(HOTEL_CLAIM.HOTEL_ID, claim.hotelId)
            .set(HOTEL_CLAIM.USER_ID, claim.userId)
            .set(HOTEL_CLAIM.STATUS, claim.status)
            .execute()



    }

    fun findBuUserIdAndHotelId(hotelId: UUID, userId: UUID): HotelClaimDTO? {
        return dslContext.selectFrom(HOTEL_CLAIM)
            .where(HOTEL_CLAIM.HOTEL_ID.eq(hotelId).and(HOTEL_CLAIM.USER_ID.eq(userId)))
            .fetchOneInto(HotelClaimDTO::class.java)
    }

    fun findByClaimId(claimId: UUID) : HotelClaimDTO? {
        return dslContext.selectFrom(HOTEL_CLAIM)
            .where(HOTEL_CLAIM.CLAIM_ID.eq(claimId))
            .fetchOneInto(HotelClaimDTO::class.java)
    }


}