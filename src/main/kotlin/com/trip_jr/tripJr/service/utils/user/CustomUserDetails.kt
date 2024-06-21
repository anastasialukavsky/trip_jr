package com.trip_jr.tripJr.service.utils.user

import com.trip_jr.tripJr.dto.user.UserTokenDTO
import com.trip_jr.tripJr.jooq.enums.UserRole
import java.util.*

data class CustomUserDetails(
val userDetails: UserTokenDTO,
) {


    fun getUserId():UUID {
        return userDetails.userId
    }

    fun getPassword():String {
       return userDetails.passwordHash
   }

    fun getUsername():String {
        return userDetails.email
    }

    fun getUserRole(): UserRole {
        return userDetails.role
    }

    fun isCredentialsNotExpired(): Boolean {
        return true
    }



//    fun isLoginExpired(): Boolean {
//        //check if the acc_token is expired against token blacklist
//        return true
//    }
//
//    fun isTokenValid(token: String): Boolean {
//
//        //decode jwt, check valid token against blacklist
//        return true
//    }
//
//    fun checkUserAdmin(role: UserRole): Boolean {
//        //check user role- ADMIN
//
//        return true
//    }
//
//    fun checkUserOwner(userRole: UserRole): Boolean {
//        //check user role - OWNER
//
//        return true
//    }

//    fun checkIsLoggedIn(user: LoggedInUserDTO): Boolean {
//        //check if user is logged in
//
//        return true
//    }

//    fun issueRefreshToken(): String {
//        //check for AC expiration date (set exp to 72hrs)
//            //issue RT if AC is expired
//                //blacklist AC, blackList RT
//    }
}