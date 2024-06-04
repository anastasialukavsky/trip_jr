package com.trip_jr.tripJr.tasks

import com.trip_jr.tripJr.jooq.enums.ClaimStatus
import com.trip_jr.tripJr.jooq.enums.UserRole
import com.trip_jr.tripJr.repository.hotel.HotelClaimRepository
import com.trip_jr.tripJr.repository.user.UserRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Component
class HotelClaimScheduler(
    private val hotelClaimRepository: HotelClaimRepository,
    private val userRepository: UserRepository
)  {

    @Scheduled(fixedRate=36_000_000) //checking every 10hrs for pending claims in the db
    fun approvePendingHotelClaims() {
        val currentTime = LocalDateTime.now()
        val pendingClaims = hotelClaimRepository.findPendingClaims()

        for (claim in pendingClaims) {
            val claimCreationTime = claim.createdAt

            if(ChronoUnit.MINUTES.between(claimCreationTime, currentTime) >= 5) {
                hotelClaimRepository.updateClaimStatus(claim.claimId, ClaimStatus.APPROVED)
                claim.userId?.let { userRepository.updateUserRole(it, UserRole.OWNER) }
            }
        }
    }
}