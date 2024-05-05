package com.trip_jr.tripJr.controller.hotel

import com.trip_jr.tripJr.dto.hotel.AmenityDTO
import com.trip_jr.tripJr.dto.hotel.updateDTOs.UpdateAmenityDTO
import com.trip_jr.tripJr.jooq.tables.Amenity
import com.trip_jr.tripJr.service.hotel.AmenityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller
import java.util.UUID

@Controller
class AmenityController {

    @Autowired
    lateinit var amenityService: AmenityService

    @MutationMapping(name = "updateAmenity")
    fun updateAmenity(
        @Argument(name = "hotelId") hotelId: UUID,
        @Argument(name="amenityId") amenityId: UUID,
        @Argument(name = "amenity") amenity: UpdateAmenityDTO)
    : AmenityDTO? {
        return amenityService.updateHotelAmenity(hotelId, amenityId, amenity)
    }
}