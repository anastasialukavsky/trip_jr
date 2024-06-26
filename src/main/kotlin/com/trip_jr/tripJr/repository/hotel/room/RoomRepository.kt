package com.trip_jr.tripJr.repository.hotel.room

import com.trip_jr.tripJr.jooq.enums.RoomStatus
import com.trip_jr.tripJr.jooq.tables.Room
import com.trip_jr.tripJr.jooq.tables.references.ROOM
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.util.UUID


@Repository
class RoomRepository(private val dsl: DSLContext) {

    fun updateRoomStatus(roomId: UUID, status: RoomStatus) {
        val affectedRows = dsl.update(ROOM)
            .set(ROOM.ROOM_STATUS, status)
            .where(ROOM.ROOM_ID.eq(roomId))
            .execute()

        if (affectedRows > 0) {
            println("Rooms status updated successfully $status")
        } else {
            println("Rooms status update failed with status $status")
        }
    }
}