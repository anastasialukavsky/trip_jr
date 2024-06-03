package com.trip_jr.tripJr.controller.hotel.hotelClaim

import com.trip_jr.tripJr.dto.hotel.HotelClaimDTO
import com.trip_jr.tripJr.repository.hotel.HotelClaimRepository
import com.trip_jr.tripJr.service.hotel.hotelClaim.HotelClaimService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class HotelClaimController {

    @Autowired
    lateinit var hotelClaimService: HotelClaimService

    @MutationMapping(name="createHotelClaim")
    fun createHotelClaim(
        @Argument(name="hotelId") hotelId: UUID,
        @Argument(name="userId") userId: UUID
    ) : HotelClaimDTO {
        return hotelClaimService.createHotelClaim(hotelId, userId)
    }

}