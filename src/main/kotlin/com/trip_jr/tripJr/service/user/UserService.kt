package com.trip_jr.tripJr.service.user

import com.trip_jr.tripJr.dto.booking.BookingDTO
import com.trip_jr.tripJr.dto.review.ReviewDTO
import com.trip_jr.tripJr.dto.user.UserDTO
//import com.trip_jr.tripJr.dto.user.UserRole
//import com.trip_jr.tripJr.jooq.enums.UserRole
import com.trip_jr.tripJr.jooq.tables.Users
import com.trip_jr.tripJr.jooq.tables.references.BOOKING
import com.trip_jr.tripJr.jooq.tables.references.REVIEW
import com.trip_jr.tripJr.jooq.tables.references.USERS
import com.trip_jr.tripJr.service.utils.PasswordUtils
import com.trip_jr.tripJr.service.utils.UUIDUtils
import com.trip_jr.tripJr.service.utils.UserUtils
import org.slf4j.LoggerFactory
import org.jooq.DSLContext
import org.jooq.User
import org.jooq.impl.DSL
import org.jooq.impl.SQLDataType
import org.mindrot.jbcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.lang.reflect.Field
import java.util.*

@Service
class UserService {

    @Autowired
    lateinit var dslContext: DSLContext

    @Autowired
    private lateinit var passwordUtils: PasswordUtils

    @Autowired
    private lateinit var UUIDUtils: UUIDUtils

    @Autowired
    private lateinit var userUtils: UserUtils


    fun getAllUsers(): List<UserDTO> {
        try {
            val users = dslContext.select()
                .from(USERS)
                .fetch()

            return users.map { record ->
                val userId = record[USERS.USER_ID]
                val email = record[USERS.EMAIL]
                val firstName = record[USERS.FIRST_NAME]
                val lastName = record[USERS.LAST_NAME]
                val passwordHash = record[USERS.PASSWORD_HASH]

                val userBookings = userId?.let { userUtils.fetchUserBookings(it) }
                val userReviews = userId?.let { userUtils.fetchUserReviews(it) }

                UserDTO(userId, email!!, firstName, lastName, passwordHash!!, userBookings, userReviews)
            }
        } catch (e: Exception) {
            throw e
        }
    }



    fun getUserById(id: UUID): UserDTO? {
        try {

            val userRecord = userUtils.getUserById(id)
            val bookingRecords =  userUtils.fetchUserBookings(id)
            val reviewRecords = userUtils.fetchUserReviews(id)


            return userRecord?.let { usersRecord ->
                UserDTO(
                    userId = usersRecord.userId,
                    email = usersRecord.email,
                    firstName = usersRecord.firstName,
                    lastName = usersRecord.lastName,
                    passwordHash = usersRecord.passwordHash,
                    bookings = bookingRecords,
                    reviews = reviewRecords
                )
            }


        } catch (e: Exception) {
            throw e
        }
    }

    fun createUser(user: UserDTO): UserDTO {
        try {

            val hashPassword = passwordUtils.hashPassword(user.passwordHash)
            val userWithHashedPassword = user.copy(passwordHash = hashPassword)

            val userId = user.userId ?: UUIDUtils.generateUUID()

            val userRecord = dslContext.insertInto(USERS)
                .set(USERS.USER_ID, userId)
                .set(USERS.EMAIL, userWithHashedPassword.email)
                .set(USERS.FIRST_NAME, userWithHashedPassword.firstName)
                .set(USERS.LAST_NAME, userWithHashedPassword.lastName)
                .set(USERS.PASSWORD_HASH, userWithHashedPassword.passwordHash)
                .returningResult(USERS.USER_ID)
                .fetchOne()

            userRecord ?: throw NullPointerException("User record cannot be null")

            return userWithHashedPassword.copy(userId = userRecord.get(USERS.USER_ID))

        } catch (e: NullPointerException) {
            throw e
        } catch (e: Exception) {
            throw e
        }
    }


    fun updateUser(id: UUID, user: UserDTO): UserDTO {
        try {
            val userRecord  = userUtils.getUserById(id)

            if (userRecord != null) {
                val hashPassword = passwordUtils.hashPassword(user.passwordHash)
                val updatedUser = user.copy(passwordHash = hashPassword)
                val updatedRecord = dslContext.update(USERS)
                    .set(USERS.EMAIL, updatedUser.email)
                    .set(USERS.FIRST_NAME, updatedUser.firstName)
                    .set(USERS.LAST_NAME, updatedUser.lastName)
                    .set(USERS.PASSWORD_HASH, updatedUser.passwordHash)
                    .where(USERS.USER_ID.eq(id))
                    .execute()

                if (updatedRecord == 1) {
                    return updatedUser
                } else {
                    throw RuntimeException("Failed to update user")
                }
            } else {
                throw RuntimeException("User record with ID $id not found")
            }
        }catch (e:Exception){
            throw e
        }
    }


}