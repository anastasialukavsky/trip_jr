package com.trip_jr.tripJr.service.hotel.room

import com.trip_jr.tripJr.dto.RoomDTO
import com.trip_jr.tripJr.dto.hotel.RateDTO
import com.trip_jr.tripJr.jooq.tables.Hotel
import com.trip_jr.tripJr.jooq.tables.Room
import com.trip_jr.tripJr.jooq.tables.references.LOCATION
import com.trip_jr.tripJr.jooq.tables.references.RATE
import com.trip_jr.tripJr.jooq.tables.references.ROOM
import com.trip_jr.tripJr.service.hotel.HotelService
import com.trip_jr.tripJr.service.utils.UUIDUtils
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*


@Service
class RoomService {

    @Autowired
    lateinit var dslContext: DSLContext
    private val logger = LoggerFactory.getLogger(HotelService::class.java)

    @Autowired
    lateinit var uuidUtils: UUIDUtils
    fun fetchRateDto(rateId: UUID?): RateDTO? {
        if (rateId == null) return null

        val rateRecord = dslContext.selectFrom(RATE)
            .where(RATE.RATE_ID.eq(rateId))
            .fetchOne()

        return rateRecord?.let {
            it[RATE.RATE_]?.let { it1 ->
                it[RATE.MONTH]?.let { it2 ->
                    it[RATE.DEFAULT_RATE]?.let { it3 ->
                        RateDTO(
                            rateId = it[RATE.RATE_ID],
                            rate = it1,
                            month = it2,
                            hotelId = it[RATE.HOTEL_ID],
                            defaultRate = it3
                        )
                    }
                }
            }
        }
    }


    fun createRoom(hotelId: UUID?, room: RoomDTO, rate: RateDTO): RoomDTO? {
        try {

            val parsedHotelId: UUID = UUID.fromString(hotelId.toString())

            if (parsedHotelId == null) {
                throw IllegalArgumentException("Hotel ID cannot be null")
            }

            val roomId = uuidUtils.generateUUID()
            logger.info("Creating room with id {}", roomId)

            val rateId = rate.rateId ?: uuidUtils.generateUUID()
            val rateRecord = dslContext.insertInto(RATE)
                .columns(
                    RATE.RATE_ID,
                    RATE.RATE_,
                    RATE.MONTH,
                    RATE.DEFAULT_RATE,
                )
                .values(
                    rateId,
                    rate.rate,
                    rate.month,
                    rate.defaultRate,
                )
                .returningResult(RATE.RATE_ID)
                .fetchOne()

            val roomRecord = dslContext.insertInto(ROOM)
                .columns(
                    ROOM.ROOM_ID,
                    ROOM.HOTEL_ID,
                    ROOM.RATE_ID,
                    ROOM.ROOM_NUMBER,
                    ROOM.ROOM_TYPE,
                    ROOM.ROOM_STATUS,
                    ROOM.BED_TYPE,
                    ROOM.MAXIMUM_OCCUPANCY,
                    ROOM.DESCRIPTION,
                    ROOM.FLOOR,
                    ROOM.AVAILABILITY,
                )
                .values(
                    roomId,
                    hotelId,
                    rateRecord?.get(RATE.RATE_ID),
                    room.roomNumber,
                    room.roomType,
                    room.roomStatus,
                    room.bedType,
                    room.maximumOccupancy,
                    room.description,
                    room.floor,
                    room.availability,
                )
                .returningResult(ROOM.ROOM_ID)
                .fetchOne()

            if (roomRecord == null) throw Exception("Failed to create room")

            val rateDto = fetchRateDto(rateRecord?.get(RATE.RATE_ID))
            return RoomDTO(
                roomId = roomId,
                hotelId = parsedHotelId,
                rate = rateDto,
                roomNumber = room.roomNumber,
                roomType = room.roomType,
                roomStatus = room.roomStatus,
                bedType = room.bedType,
                maximumOccupancy = room.maximumOccupancy,
                description = room.description,
                floor = room.floor,
                availability = room.availability,
                lastCleaned = room.lastCleaned,
                createdAt = room.createdAt,
                updatedAt = room.updatedAt
            )
        } catch (e: Exception) {
            throw e
        }
    }

}