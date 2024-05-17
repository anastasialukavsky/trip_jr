package com.trip_jr.tripJr.service.utils.auth

import com.trip_jr.tripJr.jooq.tables.references.USERS
import com.trip_jr.tripJr.service.user.UserService
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AuthUtils {

    @Autowired
    lateinit var dslContext: DSLContext

    fun fetchUserEmail(email: String): Boolean {
        try {
            val userEmail = dslContext.select()
                .from(USERS)
                .where(USERS.EMAIL.eq(email))
                .fetchOne() ?: throw RuntimeException("Email $email not found")

            return userEmail != null
        } catch (e: Exception) {
            throw e
        }
    }

    fun fetchUserPassword(passwordHash: String): String {
        try {
            return dslContext.select()
                .from(USERS)
                .where(USERS.PASSWORD_HASH.eq(passwordHash))
                .fetchOne(USERS.PASSWORD_HASH) ?: throw RuntimeException("Password not found")


        } catch (e: Exception) {
            throw e
        }
    }
}