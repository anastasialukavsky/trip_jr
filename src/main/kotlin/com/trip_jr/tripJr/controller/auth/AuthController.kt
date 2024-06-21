package com.trip_jr.tripJr.controller.auth

import com.trip_jr.tripJr.service.auth.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

@Controller
class AuthController {

    @Autowired
    lateinit var authService: AuthService

//    @MutationMapping(name="signIn")
//    fun signIn(
//        @Argument(name="email") email: String,
//        @Argument(name="password") password: String
//    ) : AuthSignInPayload? {
//        return authService.signIn(email, password)
//    }


}