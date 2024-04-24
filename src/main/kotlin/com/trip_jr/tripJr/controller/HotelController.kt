package com.trip_jr.tripJr.controller

import com.trip_jr.tripJr.dto.hotel.HotelDTO
import com.trip_jr.tripJr.service.hotel.HotelService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.*


@Controller
class HotelController {


    @Autowired
    private lateinit var hotelService: HotelService


    @QueryMapping
    fun hotelById(@Argument id: UUID): HotelDTO {
        return hotelService.getHotelById(id)
    }
}