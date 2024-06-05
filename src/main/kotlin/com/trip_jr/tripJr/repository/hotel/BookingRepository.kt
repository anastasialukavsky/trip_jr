package com.trip_jr.tripJr.repository.hotel

import com.trip_jr.tripJr.dto.booking.BookingDTO
import com.trip_jr.tripJr.dto.hotel.RoomDTO
import com.trip_jr.tripJr.jooq.tables.references.BOOKING
import com.trip_jr.tripJr.jooq.tables.references.ROOM
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime


@Repository
class BookingRepository(private val dslContext: DSLContext) {

    fun findBookingsByCheckOutDate(checkOutDate: LocalDate) : List<BookingDTO>? {
        val bookings = dslContext.select()
            .from(BOOKING)
            .join(ROOM).on(BOOKING.ROOM_ID.eq(ROOM.ROOM_ID))
            .where(BOOKING.CHECK_OUT_DATE.eq(checkOutDate))
            .fetch{ record ->
                record[BOOKING.GUEST_FIRST_NAME]?.let {
                    record[BOOKING.GUEST_LAST_NAME]?.let { it1 ->
                        record[BOOKING.NUM_OF_GUESTS]?.let { it2 ->
                            record[BOOKING.CHECK_IN_DATE]?.let { it3 ->
                                record[BOOKING.CHECK_OUT_DATE]?.let { it4 ->
                                    BookingDTO(
                                        bookingId = record[BOOKING.BOOKING_ID],
                                        userId = record[BOOKING.USER_ID],
                                        hotelId = record[BOOKING.HOTEL_ID],
                                        guestFirstName = it,
                                        guestLastName = it1,
                                        numOfGuests = it2,
                                        occasion = record[BOOKING.OCCASION],
                                        guestNotes = record[BOOKING.GUEST_NOTES],
                                        checkInDate = it3,
                                        checkOutDate = it4,
                                        totalCost = record[BOOKING.TOTAL_COST],
                                        roomDetails = record[ROOM.HOTEL_ID]?.let { it5 ->
                                            record[ROOM.ROOM_NUMBER]?.let { it6 ->
                                                record[ROOM.ROOM_TYPE]?.let { it7 ->
                                                    record[ROOM.ROOM_STATUS]?.let { it8 ->
                                                        record[ROOM.BED_TYPE]?.let { it9 ->
                                                            record[ROOM.MAXIMUM_OCCUPANCY]?.let { it10 ->
                                                                record[ROOM.DESCRIPTION]?.let { it11 ->
                                                                    record[ROOM.FLOOR]?.let { it12 ->
                                                                        record[ROOM.AVAILABILITY]?.let { it13 ->
                                                                            record[ROOM.CREATED_AT]?.toLocalDateTime()
                                                                                ?.let { it14 ->
                                                                                    record[ROOM.UPDATED_AT]?.toLocalDateTime()
                                                                                        ?.let { it15 ->
                                                                                            RoomDTO(
                                                                                                roomId = record[ROOM.ROOM_ID],
                                                                                                hotelId = it5,
                                                                                                rate = null,
                                                                                                roomNumber = it6,
                                                                                                roomType = it7,
                                                                                                roomStatus = it8,
                                                                                                bedType = it9,
                                                                                                maximumOccupancy = it10,
                                                                                                description = it11,
                                                                                                floor = it12,
                                                                                                availability = it13,
                                                                                                lastCleaned = record[ROOM.LAST_CLEANED]?.toLocalDateTime(),
                                                                                                createdAt = it14,
                                                                                                updatedAt = it15
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
                                            }
                                        },
                                        createdAt = record[BOOKING.CREATED_AT]?.toLocalDateTime()!!,
                                        updatedAt = record[BOOKING.UPDATED_AT]?.toLocalDateTime()!!
                                    )
                                }
                            }
                        }
                    }
                }
            }

        return bookings
    }


}