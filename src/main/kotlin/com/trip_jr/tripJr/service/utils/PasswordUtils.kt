package com.trip_jr.tripJr.service.utils

import org.mindrot.jbcrypt.BCrypt
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class PasswordUtils {

    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt(10))
    }
}