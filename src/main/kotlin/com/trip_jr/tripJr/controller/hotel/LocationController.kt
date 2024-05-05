package com.trip_jr.tripJr.controller.hotel

import com.trip_jr.tripJr.dto.hotel.LocationDTO
import com.trip_jr.tripJr.dto.hotel.updateDTOs.UpdateLocationDTO
import com.trip_jr.tripJr.service.hotel.LocationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller
import java.util.*


@Controller
class LocationController {

    @Autowired
    private lateinit var locationService: LocationService

    @MutationMapping(name="updateLocation")
    fun updateLocation(
        @Argument(name="id") id: UUID,
        @Argument(name="location") location: UpdateLocationDTO): LocationDTO? {
        return locationService.updateLocation(id, location)
    }

}