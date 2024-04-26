package com.trip_jr.tripJr.service.user

import com.trip_jr.tripJr.dto.user.UserDTO
//import com.trip_jr.tripJr.dto.user.UserRole
//import com.trip_jr.tripJr.jooq.enums.UserRole
import com.trip_jr.tripJr.jooq.tables.Users
import com.trip_jr.tripJr.jooq.tables.references.USERS
import org.slf4j.LoggerFactory
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.jooq.impl.SQLDataType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.reflect.Field
import java.util.*

@Service
class UserService {

    @Autowired
    lateinit var dslContext: DSLContext

    private val logger = LoggerFactory.getLogger(UserService::class.java)


    fun generateUniqueUUID(): UUID {
        return UUID.randomUUID()
    }


    fun createUser(user: UserDTO): UserDTO? {
        try {
            val userId = user.userId ?: generateUniqueUUID()

            val userRecord = dslContext.insertInto(USERS)
                .set(USERS.USER_ID, userId)
                .set(USERS.EMAIL, user.email)
                .set(USERS.FIRST_NAME, user.firstName)
                .set(USERS.LAST_NAME, user.lastName)
                .set(USERS.PASSWORD_HASH, user.passwordHash)
                .returningResult(USERS.USER_ID)
                .fetchOne()

            if (userRecord == null) {
                throw NullPointerException("Failed to create USER")
            }

            return userRecord.get(USERS.USER_ID)?.let {
                UserDTO(
                    userId = it,
                    email = user.email,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    passwordHash = user.passwordHash,
                )
            }
        } catch (e: NullPointerException) {
            throw e
        } catch (e: Exception) {
            throw e
        }
    }



}