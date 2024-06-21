package com.trip_jr.tripJr.service.auth

//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import com.trip_jr.tripJr.service.utils.auth.AuthUtils
import com.trip_jr.tripJr.service.utils.auth.PasswordUtils
import com.trip_jr.tripJr.service.utils.jwt.JwtUtils
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AuthService {


//    @Autowired
//    private lateinit var customUserDetailsService: CustomUserDetailsService

//    @Autowired
//    private lateinit var authenticationManagerBuilder: AuthenticationManagerBuilder

    @Autowired
    lateinit var dslContext: DSLContext

    @Autowired
    lateinit var authUtils: AuthUtils

    @Autowired
    lateinit var passwordUtils: PasswordUtils

    @Autowired
    lateinit var jwtUtils: JwtUtils


//    fun signIn(email: String, password: String): AuthSignInPayload? {
//        try {
//            val userDetails = customUserDetailsService.getUserByEmail(email)
//                ?: throw RuntimeException("User with email $email not found")
//
//            if (passwordUtils.checkPassword(password, userDetails.passwordHash)) {
//                val token = generateToken(userDetails)
//                return AuthSignInPayload(token, userDetails.email)
//            } else {
//                throw RuntimeException("Incorrect password")
//            }
//        } catch (e: Exception) {
//            throw e
//        }
//    }




}