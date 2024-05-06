package com.trip_jr.tripJr.controller.user

import com.trip_jr.tripJr.dto.user.UserDTO
import com.trip_jr.tripJr.service.user.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import java.util.*


@Controller
class UserController {

    @Autowired
    private lateinit var userService: UserService


    @QueryMapping(name = "users")
    fun users(): List<UserDTO> {
        return userService.getAllUsers()
    }

    @QueryMapping(name = "userById")
    fun userById(@Argument(name = "id") id: UUID): UserDTO? {
        return userService.getUserById(id)
    }

    @MutationMapping(name = "createUser")
    fun createUser(@Argument(name = "user") user: UserDTO): UserDTO {
        return userService.createUser(user)
    }

    @MutationMapping(name = "updateUser")
    fun updateUser(@Argument(name = "id") id: UUID, @Argument(name = "user") user: UserDTO): UserDTO {
        return userService.updateUser(id, user)
    }

    @MutationMapping(name = "deleteUser")
    fun deleteUser(@Argument(name = "id") id: UUID) : Boolean {
        return userService.deleteUser(id)
    }

}