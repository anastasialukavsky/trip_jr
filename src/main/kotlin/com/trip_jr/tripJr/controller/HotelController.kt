package com.trip_jr.tripJr.controller

import com.trip_jr.tripJr.dto.hotel.HotelDTO
import com.trip_jr.tripJr.service.hotel.HotelService
import jakarta.validation.Valid
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
class HotelController {


    @Autowired
    private lateinit var hotelService: HotelService


    @QueryMapping
    fun hotelById(@Argument id: UUID): HotelDTO {
        return hotelService.getHotelById(id)
    }


    @MutationMapping
    fun createHotel(@Argument hotel: HotelDTO): ResponseEntity<HotelDTO> {
        val createdHotel = hotelService.createHotel(hotel)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHotel)
    }
}