package com.trip_jr.tripJr.service.user

import com.trip_jr.tripJr.dto.user.UserDTO
//import com.trip_jr.tripJr.dto.user.UserRole
//import com.trip_jr.tripJr.jooq.enums.UserRole
import com.trip_jr.tripJr.jooq.tables.Users
import com.trip_jr.tripJr.jooq.tables.references.USERS
import com.trip_jr.tripJr.service.utils.PasswordUtils
import org.slf4j.LoggerFactory
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.jooq.impl.SQLDataType
import org.mindrot.jbcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.reflect.Field
import java.util.*

@Service
class UserService {

    @Autowired
    lateinit var dslContext: DSLContext


    @Autowired
    private lateinit var passwordUtils: PasswordUtils

    private val logger = LoggerFactory.getLogger(UserService::class.java)


    fun generateUniqueUUID(): UUID {
        return UUID.randomUUID()
    }


    fun createUser(user: UserDTO): UserDTO {
        try {

            val hashPassword = passwordUtils.hashPassword(user.passwordHash)
            val userWithHashedPassword = user.copy(passwordHash = hashPassword)

            val userId = user.userId ?: generateUniqueUUID()

            val userRecord = dslContext.insertInto(USERS)
                .set(USERS.USER_ID, userId)
                .set(USERS.EMAIL, userWithHashedPassword.email)
                .set(USERS.FIRST_NAME, userWithHashedPassword.firstName)
                .set(USERS.LAST_NAME, userWithHashedPassword.lastName)
                .set(USERS.PASSWORD_HASH, userWithHashedPassword.passwordHash)
                .returningResult(USERS.USER_ID)
                .fetchOne()

            userRecord ?: throw NullPointerException("User record was null.")

            return userWithHashedPassword.copy(userId = userRecord.get(USERS.USER_ID))

        } catch (e: NullPointerException) {
            logger.error("NULL POINTER", e)
            throw e
        } catch (e: Exception) {
            logger.error("failed to create user", e)
            throw e
        }
    }


}