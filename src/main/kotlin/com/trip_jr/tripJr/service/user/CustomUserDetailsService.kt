package com.trip_jr.tripJr.service.user

import com.amazonaws.services.kms.model.NotFoundException
import com.trip_jr.tripJr.dto.user.UserTokenDTO
import com.trip_jr.tripJr.repository.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService {

    @Autowired
    lateinit var userRepository: UserRepository

    fun getUserByEmail(email: String): UserTokenDTO? {
        try {
            val userFound = userRepository.getUserByEmail(email) ?: throw NotFoundException("User with email $email not found")

            return userFound.userId?.let {
                userFound.role?.let { it1 ->
                    UserTokenDTO(
                        userId = it,
                        email = userFound.email,
                        passwordHash = userFound.passwordHash,
                        role = it1
                    )
                }
            }
        }catch(e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}