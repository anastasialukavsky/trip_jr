package com.trip_jr.tripJr.controller.hotel

import com.trip_jr.tripJr.service.hotel.RateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller
import java.util.UUID


@Controller
class RateController {

    @Autowired
    lateinit var rateService: RateService

    @MutationMapping(name="deleteRate")
    fun deleteRate(@Argument(name="id") id: UUID) : Boolean {
        return rateService.deleteRate(id)
    }

}