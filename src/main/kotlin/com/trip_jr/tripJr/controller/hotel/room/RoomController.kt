package com.trip_jr.tripJr.controller.hotel.room

import com.trip_jr.tripJr.dto.RoomDTO
import com.trip_jr.tripJr.dto.hotel.RateDTO
import com.trip_jr.tripJr.service.hotel.room.RoomService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import java.util.*


@Controller
class RoomController {

    @Autowired
    private lateinit var roomService: RoomService

    @QueryMapping(name="roomsByHotelId")
    fun roomsByHotelId(@Argument(name="hotelId") hotelId: UUID): MutableList<RoomDTO> {
        return roomService.roomsByHotelId(hotelId)
    }

    @QueryMapping(name="roomById")
    fun roomById(@Argument(name="id") roomId: UUID): RoomDTO? {
        return roomService.roomById(roomId)
    }

    @MutationMapping(name="createRoom")
    fun createRoom(
        @Argument(name="hotelId") hotelId: UUID,
        @Argument(name="room") room:  RoomDTO,
//        @Argument(name="rate") rate: RateDTO,
        ): RoomDTO? {
        return roomService.createRoom(hotelId, room)
    }

    @MutationMapping(name="updateRoom")
    fun updateRoom(
        @Argument(name="hotelId") hotelId: UUID,
        @Argument(name="roomId") roomId: UUID,
        @Argument(name="room") room: UpdateRoomDTO,
    ): RoomDTO? {
        return roomService.updateRoom(hotelId, roomId, room)
    }

    @MutationMapping(name="deleteRoom")
    fun deleteRoom(@Argument(name="id") id: UUID) : Boolean {
        return roomService.deleteRoom(id)
    }
}