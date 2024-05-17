package com.trip_jr.tripJr.service.hotel.room

import com.trip_jr.tripJr.controller.hotel.room.UpdateRoomDTO
import com.trip_jr.tripJr.dto.hotel.RoomDTO
import com.trip_jr.tripJr.dto.hotel.RateDTO
import com.trip_jr.tripJr.jooq.enums.RoomStatus
import com.trip_jr.tripJr.jooq.tables.references.RATE
import com.trip_jr.tripJr.jooq.tables.references.ROOM
import com.trip_jr.tripJr.service.hotel.HotelService
import com.trip_jr.tripJr.service.utils.RateUtils
import com.trip_jr.tripJr.service.utils.UUIDUtils
import org.jooq.DSLContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*


@Service
class RoomService {

    @Autowired
    lateinit var dslContext: DSLContext

    @Autowired
    lateinit var rateUtils: RateUtils


    private val logger = LoggerFactory.getLogger(HotelService::class.java)

    @Autowired
    lateinit var uuidUtils: UUIDUtils


    fun roomsByHotelId(hotelId: UUID): MutableList<RoomDTO> {
        try {
            val roomsRecord = dslContext.select()
                .from(ROOM)
                .join(RATE).on(ROOM.RATE_ID.eq(RATE.RATE_ID))
                .where(ROOM.HOTEL_ID.eq(hotelId))
                .fetch()

            return roomsRecord.map { record ->
                val roomId = record[ROOM.ROOM_ID]
                val hotelId = record[ROOM.HOTEL_ID]
                val rate = record[RATE.RATE_]?.let {
                    record[RATE.MONTH]?.let { it1 ->
                        record[RATE.DEFAULT_RATE]?.let { it2 ->
                            RateDTO(
                                rateId = record[RATE.RATE_ID],
                                rate = it,
                                month = it1,
                                defaultRate = it2
                            )
                        }
                    }
                }
                val roomNumber = record[ROOM.ROOM_NUMBER]
                val roomType = record[ROOM.ROOM_TYPE]
                val roomStatus = record[ROOM.ROOM_STATUS]
                val bedType = record[ROOM.BED_TYPE]
                val maximumOccupancy = record[ROOM.MAXIMUM_OCCUPANCY]
                val description = record[ROOM.DESCRIPTION]
                val floor = record[ROOM.FLOOR]
                val availability = record[ROOM.AVAILABILITY]

                RoomDTO(
                    roomId,
                    hotelId!!,
                    rate,
                    roomNumber!!,
                    roomType!!,
                    roomStatus!!,
                    bedType!!,
                    maximumOccupancy!!,
                    description!!,
                    floor!!,
                    availability!!
                )
            }

        } catch (e: Exception) {
            throw e
        }
    }


    fun roomById(roomId: UUID): RoomDTO? {
        try {
            val roomRecord = dslContext.select()
                .from(ROOM)
                .where(ROOM.ROOM_ID.eq(roomId))
                .fetchOne()

            if (roomRecord != null) {
                val rateId = roomRecord.get(ROOM.RATE_ID) // Assuming rate_id is stored in roomRecord
                val rateRecord = dslContext.select()
                    .from(RATE)
                    .where(RATE.RATE_ID.eq(rateId))
                    .fetchOne()

                return if (rateRecord != null) {
                    roomRecord.get(ROOM.HOTEL_ID)?.let { hotelId ->
                        roomRecord.get(ROOM.ROOM_NUMBER)?.let { roomNumber ->
                            roomRecord.get(ROOM.ROOM_TYPE)?.let { roomType ->
                                roomRecord.get(ROOM.ROOM_STATUS)?.let { roomStatus ->
                                    roomRecord.get(ROOM.BED_TYPE)?.let { bedType ->
                                        roomRecord.get(ROOM.DESCRIPTION)?.let { description ->
                                            roomRecord.get(ROOM.AVAILABILITY)?.let { availability ->
                                                roomRecord.get(ROOM.CREATED_AT)?.toLocalDateTime()?.let { createdAt ->
                                                    RoomDTO(
                                                        roomId = roomId,
                                                        hotelId = hotelId,
                                                        rate = rateRecord.get(RATE.RATE_)?.let {
                                                            rateRecord.get(RATE.MONTH)?.let { it1 ->
                                                                RateDTO(
                                                                    rateId = rateRecord.get(RATE.RATE_ID),
                                                                    rate = it,
                                                                    month = it1,
                                                                    defaultRate = rateRecord.get(RATE.DEFAULT_RATE)!!
                                                                )
                                                            }
                                                        },
                                                        roomNumber = roomNumber,
                                                        roomType = roomType,
                                                        roomStatus = roomStatus,
                                                        bedType = bedType,
                                                        maximumOccupancy = roomRecord.get(ROOM.MAXIMUM_OCCUPANCY)!!,
                                                        description = description,
                                                        floor = roomRecord.get(ROOM.FLOOR)!!,
                                                        availability = availability,
                                                        lastCleaned = roomRecord.get(ROOM.LAST_CLEANED)?.toLocalDateTime(),
                                                        createdAt = createdAt,
                                                        updatedAt = roomRecord.get(ROOM.UPDATED_AT)?.toLocalDateTime()!!
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    throw IllegalStateException("Rate record not found for roomId: $roomId")
                }
            }
            return null
        } catch (e: Exception) {
            throw e
        }
    }

    fun getAvailableRoomsByHotelId(hotelId: UUID): MutableList<RoomDTO> {
        try {
            val availableRoomsRecord = dslContext.select()
                .from(ROOM)
                .where(ROOM.HOTEL_ID.eq(hotelId))
                .and(ROOM.AVAILABILITY.eq(true))
                .and(ROOM.ROOM_STATUS.eq(RoomStatus.Vacant))
                .fetch()

            val availableRooms = mutableListOf<RoomDTO>()
            for (record in availableRoomsRecord) {
                record.get(ROOM.HOTEL_ID)?.let {
                    record.get(ROOM.ROOM_NUMBER)?.let { it1 ->
                        record.get(ROOM.ROOM_TYPE)?.let { it2 ->
                            record.get(ROOM.BED_TYPE)?.let { it3 ->
                                record.get(ROOM.DESCRIPTION)?.let { it4 ->
                                    record.get(ROOM.AVAILABILITY)?.let { it5 ->
                                        record.get(ROOM.CREATED_AT)?.let { it6 ->
                                            RoomDTO(
                                                roomId = record.get(ROOM.ROOM_ID),
                                                hotelId = it,
                                                roomNumber = it1,
                                                roomType = it2,
                                                roomStatus = RoomStatus.valueOf(record.get(ROOM.ROOM_STATUS).toString()),
                                                bedType = it3,
                                                maximumOccupancy = record.get(ROOM.MAXIMUM_OCCUPANCY)!!,
                                                description = it4,
                                                floor = record.get(ROOM.FLOOR)!!,
                                                availability = it5,
                                                lastCleaned = record.get(ROOM.LAST_CLEANED)?.toLocalDateTime(),
                                                createdAt = it6.toLocalDateTime(),
                                                updatedAt = record.get(ROOM.UPDATED_AT)!!.toLocalDateTime()
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }?.let {
                    availableRooms.add(
                        it
                    )
                }
            }

            return availableRooms

        }catch(e: Exception) {
            throw e
        }
    }

    fun createRoom(hotelId: UUID?, room: RoomDTO): RoomDTO? {
        try {

            val parsedHotelId = uuidUtils.parseUUID(hotelId)


            val roomId = uuidUtils.generateUUID()
            logger.info("Creating room with id {}", roomId)


            val rateId =  uuidUtils.generateUUID()
            val rateRecord = dslContext.insertInto(RATE)
                .columns(
                    RATE.RATE_ID,
                    RATE.RATE_,
                    RATE.MONTH,
                    RATE.DEFAULT_RATE,
                )
                .values(
                    rateId,
                    room.rate?.rate,
                    room.rate?.month,
                    room.rate?.defaultRate,
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

            val rateDto = rateUtils.fetchRateDto(rateRecord?.get(RATE.RATE_ID))
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

    fun updateRoom(hotelId: UUID, roomId: UUID, room: UpdateRoomDTO): RoomDTO? {
        try {
            val parsedHotelId = uuidUtils.parseUUID(hotelId)
            val parsedRoomId = uuidUtils.parseUUID(roomId)
            val roomRecord = dslContext.select()
                .from(ROOM)
                .join(RATE).on(ROOM.RATE_ID.eq(RATE.RATE_ID))
                .where(ROOM.ROOM_ID.eq(parsedRoomId))
                .fetchOne() ?: throw RuntimeException("Room with ID $roomId not found")

            val currentTimestamp = OffsetDateTime.now(ZoneOffset.UTC)

            val rate = roomRecord.into(RateDTO::class.java)

            val originalRoomRecord: RoomDTO? = roomRecord.get(ROOM.ROOM_NUMBER)?.let {
                roomRecord.get(ROOM.ROOM_TYPE)?.let { it1 ->
                    roomRecord.get(ROOM.ROOM_STATUS)?.let { it2 ->
                        roomRecord.get(ROOM.BED_TYPE)?.let { it3 ->
                            roomRecord.get(ROOM.DESCRIPTION)?.let { it4 ->
                                roomRecord.get(ROOM.AVAILABILITY)?.let { it5 ->
                                    roomRecord.get(ROOM.LAST_CLEANED)?.toLocalDateTime()?.let { it6 ->
                                        RoomDTO(
                                            parsedRoomId,
                                            parsedHotelId,
                                            rate = rate,
                                            roomNumber = it,
                                            roomType = it1,
                                            roomStatus = it2,
                                            bedType = it3,
                                            maximumOccupancy = roomRecord.get(ROOM.MAXIMUM_OCCUPANCY)!!,
                                            description = it4,
                                            floor = roomRecord.get(ROOM.FLOOR)!!,
                                            availability = it5,
                                            lastCleaned = it6,
                                            createdAt = roomRecord.get(ROOM.CREATED_AT)?.toLocalDateTime()!!,
                                            updatedAt = roomRecord.get(ROOM.UPDATED_AT)?.toLocalDateTime()!!,

                                            )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            val updatedRoomRecord = (room.bedType ?: originalRoomRecord?.bedType)?.let {
                (room.roomNumber ?: originalRoomRecord?.roomNumber)?.let { it1 ->
                    (room.roomType ?: originalRoomRecord?.roomType)?.let { it2 ->
                        (room.roomStatus ?: originalRoomRecord?.roomStatus)?.let { it3 ->
                            (room.maximumOccupancy ?: originalRoomRecord?.maximumOccupancy)?.let { it4 ->
                                (room.description ?: originalRoomRecord?.description)?.let { it5 ->
                                    (room.floor ?: originalRoomRecord?.floor)?.let { it6 ->
                                        (room.availability ?: originalRoomRecord?.availability)?.let { it7 ->
                                            (room.lastCleaned ?: originalRoomRecord?.lastCleaned)?.let { it8 ->
                                                originalRoomRecord?.copy(
                                                    rate = rate,
                                                    roomNumber = it1,
                                                    roomType = it2,
                                                    roomStatus = it3,
                                                    bedType = it,
                                                    maximumOccupancy = it4,
                                                    description = it5,
                                                    floor = it6,
                                                    availability = it7,
                                                    lastCleaned = it8,
                                                    updatedAt = currentTimestamp.toLocalDateTime()
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }


            val updateQuery = dslContext.update(ROOM)
                .set(ROOM.ROOM_NUMBER, updatedRoomRecord?.roomNumber)
                .set(ROOM.ROOM_TYPE, updatedRoomRecord?.roomType)
                .set(ROOM.ROOM_STATUS, updatedRoomRecord?.roomStatus)
                .set(ROOM.BED_TYPE, updatedRoomRecord?.bedType)
                .set(ROOM.MAXIMUM_OCCUPANCY, updatedRoomRecord?.maximumOccupancy)
                .set(ROOM.DESCRIPTION, updatedRoomRecord?.description)
                .set(ROOM.FLOOR, updatedRoomRecord?.floor)
                .set(ROOM.AVAILABILITY, updatedRoomRecord?.availability)
                .set(ROOM.UPDATED_AT, currentTimestamp)
                .where(ROOM.ROOM_ID.eq(parsedRoomId))
                .execute()

            return if (updateQuery == 1) updatedRoomRecord
            else throw RuntimeException("Failed to update room with ID $roomId")

        } catch (e: Exception) {
            throw e
        }
    }

    fun deleteRoom(id: UUID): Boolean {
        try {
            val deleteRecordCount = dslContext.deleteFrom(ROOM)
                .where(ROOM.ROOM_ID.eq(id))
                .execute()

            return deleteRecordCount == 1

        } catch (e: Exception) {
            throw e
        }
    }
}