package com.trip_jr.tripJr.service.utils.auth

import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Component

@Component
class PasswordUtils {

    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt(10))
    }

    fun checkPassword(plainPassword: String, passwordHash: String) : Boolean {
        return BCrypt.checkpw(plainPassword, passwordHash)
    }
}