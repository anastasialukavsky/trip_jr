package com.trip_jr.tripJr.controller.user

import com.trip_jr.tripJr.dto.user.UserDTO
import com.trip_jr.tripJr.service.user.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody


@Controller
class UserController {

    @Autowired
    private lateinit var userService: UserService

    private val logger = LoggerFactory.getLogger(UserService::class.java)
    @MutationMapping(name = "createUser")
    fun createUser(@Argument(name="user") user: UserDTO): UserDTO? {
        return userService.createUser(user)

    }

}