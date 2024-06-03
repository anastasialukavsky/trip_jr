package com.trip_jr.tripJr.service.hotel.hotelClaim

import com.trip_jr.tripJr.dto.hotel.HotelClaimDTO
import com.trip_jr.tripJr.jooq.enums.ClaimStatus
import com.trip_jr.tripJr.repository.hotel.HotelClaimRepository
import com.trip_jr.tripJr.repository.user.UserRepository
import com.trip_jr.tripJr.service.utils.UUIDUtils
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class HotelClaimService {

    @Autowired
    lateinit var dslContext: DSLContext

    @Autowired
    lateinit var uuidUtils: UUIDUtils

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var hotelClaimRepository: HotelClaimRepository

    @Transactional
    fun createHotelClaim(hotelId: UUID, userId: UUID): HotelClaimDTO {


        try {
            val user = userRepository.getUserById(userId) ?: throw RuntimeException("user with ID $userId not found")

            val checkExistingClaim = hotelClaimRepository.findBuUserIdAndHotelId(hotelId, userId)

            if (checkExistingClaim != null) {
                throw RuntimeException("hotel claim already exists")
            }

            val claimId = uuidUtils.generateUUID()

            val claim = HotelClaimDTO(claimId, hotelId, userId, status = ClaimStatus.PENDING)

            return hotelClaimRepository.saveHotelClaim(claim)
        } catch (e: Exception) {
            throw e
        }
    }

}