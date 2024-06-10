package com.trip_jr.tripJr.controller.hotel

import com.trip_jr.tripJr.dto.hotel.HotelDTO
import com.trip_jr.tripJr.dto.hotel.updateDTOs.UpdateHotelDTO
import com.trip_jr.tripJr.service.aws.S3Service
import com.trip_jr.tripJr.service.hotel.HotelService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.multipart.MultipartFile
import java.util.*


@Controller
class HotelController {


    @Autowired
    private lateinit var s3Service: S3Service

    @Autowired
    private lateinit var hotelService: HotelService


    @QueryMapping(name = "hotels")
    fun hotels(): List<HotelDTO> {
        return hotelService.getAllHotels()
    }

    @QueryMapping
    fun hotelById(@Argument id: UUID): HotelDTO? {
        return hotelService.getHotelById(id)
    }


    @MutationMapping(name = "createHotel")
    fun createHotel(
        @Argument(name = "hotel") hotel: HotelDTO,
        @Argument(name="hotelImageURLs") hotelImageURLs: List<MultipartFile>
        ): HotelDTO? {
        val imageURLs = s3Service.uploadImages(hotelImageURLs)
        return hotelService.createHotel(hotel, imageURLs)
    }

    @MutationMapping(name = "updateHotel")
    fun updateHotel(
        @Argument(name = "id") id: UUID,
        @Argument(name = "hotel") hotel: UpdateHotelDTO
    )
    : HotelDTO? {
        return hotelService.updateHotel(id, hotel)
    }

    @MutationMapping(name = "deleteHotel")
    fun deleteHotel(@Argument id: UUID): Boolean {
        return hotelService.deleteHotel(id)
    }
}