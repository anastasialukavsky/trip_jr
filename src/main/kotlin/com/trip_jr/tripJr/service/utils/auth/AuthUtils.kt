package com.trip_jr.tripJr.service.utils.auth

import com.trip_jr.tripJr.jooq.tables.references.USERS
import com.trip_jr.tripJr.service.hotel.LocationService
import com.trip_jr.tripJr.service.user.UserService
import org.jooq.DSLContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AuthUtils {

    @Autowired
    lateinit var dslContext: DSLContext

    private val logger = LoggerFactory.getLogger(LocationService::class.java)
    fun fetchUserEmail(email: String): Boolean {
        try {
            val userEmail = dslContext.select()
                .from(USERS)
                .where(USERS.EMAIL.eq(email))
                .fetchOne() ?: throw RuntimeException("Email $email not found")

            logger.info("Fetching user email: $userEmail")

            return userEmail != null
        } catch (e: Exception) {
            throw e
        }
    }



    fun fetchUserPassword(email: String, passwordHash: String): String {
        try {
            val hashedPassword = dslContext.select(USERS.PASSWORD_HASH)
                .from(USERS)
                .where(USERS.EMAIL.eq(email).and(USERS.PASSWORD_HASH.eq(passwordHash)))
                .fetchOne(USERS.PASSWORD_HASH) ?: throw RuntimeException("Password not found")

            return hashedPassword

        } catch (e: Exception) {
            throw e
        }
    }

    fun fetchUserCredentials(email: String, password: String): Pair<String?, String?> {
        try {

            val userRecord  = dslContext.select(USERS.EMAIL, USERS.PASSWORD_HASH)
                .from(USERS)
                .where(USERS.EMAIL.eq(email))
                .fetchOne() ?: throw RuntimeException("User with email $email not found")

            val userEmail = userRecord.get(USERS.EMAIL)
            val userPassword = userRecord.get(USERS.PASSWORD_HASH)

            return userEmail to userPassword

        }catch(e: Exception) {
            throw e
        }
    }
}