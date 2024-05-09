package com.trip_jr.tripJr.controller.hotel.room

import com.trip_jr.tripJr.dto.RoomDTO
import com.trip_jr.tripJr.dto.hotel.RateDTO
import com.trip_jr.tripJr.service.hotel.room.RoomService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.*


@Controller
class RoomController {

    @Autowired
    private lateinit var roomService: RoomService

    @MutationMapping(name="createRoom")
    fun createRoom(
        @Argument(name="hotelId") hotelId: UUID,
        @Argument(name="room") room:  RoomDTO,
        @Argument(name="rate") rate: RateDTO,
        ): RoomDTO? {
        return roomService.createRoom(hotelId, room, rate)
    }
}