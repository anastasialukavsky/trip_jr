package com.trip_jr.tripJr.service.auth

import com.trip_jr.tripJr.dto.auth.AuthSignInPayload
import com.trip_jr.tripJr.service.utils.auth.AuthUtils
import com.trip_jr.tripJr.service.utils.auth.PasswordUtils
import com.trip_jr.tripJr.service.utils.jwt.JwtUtils
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AuthService {


    @Autowired
    lateinit var dslContext: DSLContext

    @Autowired
    lateinit var authUtils: AuthUtils

    @Autowired
    lateinit var passwordUtils: PasswordUtils

    @Autowired
    lateinit var jwtUtils: JwtUtils

    fun signIn(email : String, password : String) : AuthSignInPayload? {
        try {

            authUtils.fetchUserCredentials(email, password).let { (userEmail, userPassword) ->
                if (userPassword?.let { passwordUtils.checkPassword(password, it) } == true) {
                    val token = jwtUtils.generateToken(email)
                    return userEmail?.let { AuthSignInPayload(token, it) }
                } else {
                    throw RuntimeException("Incorrect password")
                }
            }

        }catch(e: Exception) {
            throw e
        }
    }
}