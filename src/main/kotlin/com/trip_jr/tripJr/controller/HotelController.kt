package com.trip_jr.tripJr.controller

import com.trip_jr.tripJr.dto.hotel.HotelDTO
import com.trip_jr.tripJr.jooq.tables.Hotel
import com.trip_jr.tripJr.service.hotel.HotelService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/hotels")
class HotelController {


    @Autowired
    private lateinit var hotelService: HotelService


    @GetMapping("/")
    fun getAllHotels(): List<Hotel> {
        return hotelService.getAllHotels()
    }

    @GetMapping("/{id}")
    fun getHotelById(@PathVariable("id") hotelId: UUID):  ResponseEntity<HotelDTO> {
        val hotel =  hotelService.getHotelById(hotelId)
        return ResponseEntity.status(HttpStatus.OK).body(hotel)
    }

    @PostMapping("")
    fun createHotel(@Valid @RequestBody hotel: HotelDTO): ResponseEntity<HotelDTO> {
        val createdHotel = hotelService.createHotel(hotel)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHotel)
    }

}