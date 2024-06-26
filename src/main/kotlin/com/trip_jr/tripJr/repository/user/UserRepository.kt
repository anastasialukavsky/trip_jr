package com.trip_jr.tripJr.repository.user

import com.trip_jr.tripJr.dto.user.UserDTO
import com.trip_jr.tripJr.jooq.enums.UserRole
import com.trip_jr.tripJr.jooq.tables.references.USERS
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserRepository(private val dslContext: DSLContext) {

    fun getUserById(userId: UUID): UserDTO? {
        return dslContext.selectFrom(USERS)
            .where(USERS.USER_ID.eq(userId))
            .fetchOneInto(UserDTO::class.java)
    }

    fun updateUserRole(userId: UUID, role: UserRole) {
        dslContext.update(USERS)
            .set(USERS.ROLE, role)
            .where(USERS.USER_ID.eq(userId))
            .execute()
    }

    fun getUserByEmail(email: String): UserDTO? {
        return dslContext.selectFrom(USERS)
            .where(USERS.EMAIL.eq(email))
            .fetchOneInto(UserDTO::class.java)
    }


}