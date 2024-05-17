package com.trip_jr.tripJr.service.auth

import com.trip_jr.tripJr.dto.auth.AuthSignInPayload
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AuthService {


    @Autowired
    lateinit var dslContext: DSLContext

    fun signIn(email : String, password : String) : AuthSignInPayload {
        try {

        }catch(e: Exception) {
            throw e
        }
    }
}