package com.trip_jr.tripJr.repository.hotel

import com.trip_jr.tripJr.dto.amenities.AmenitySummarySearchDTO
import com.trip_jr.tripJr.dto.booking.BookingDTO
import com.trip_jr.tripJr.dto.hotel.HotelSearchDTO
import com.trip_jr.tripJr.dto.hotel.RateDTO
import com.trip_jr.tripJr.dto.hotel.RoomDTO
import com.trip_jr.tripJr.dto.hotel.room.RoomSummarySearchDTO
import com.trip_jr.tripJr.dto.location.LocationSummarySearchDTO
import com.trip_jr.tripJr.jooq.tables.references.*
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class HotelRepository(
    private val dslContext: DSLContext,
) {

    fun getHotelRoomsByHotelId(hotelId: UUID): MutableList<RoomDTO>? {
        return dslContext.selectFrom(ROOM)
            .where(ROOM.ROOM_ID.eq(hotelId))
            .fetchInto(RoomDTO::class.java)
    }

    fun getHotelBookings(hotelId: UUID): MutableList<BookingDTO> {
        try {
            val bookingsRecord = dslContext.select()
                .from(BOOKING)
                .join(ROOM).on(BOOKING.ROOM_ID.eq(ROOM.ROOM_ID))
                .join(RATE).on(ROOM.RATE_ID.eq(RATE.RATE_ID))
                .where(BOOKING.HOTEL_ID.eq(hotelId))
                .fetch { record ->
                    record.get(BOOKING.GUEST_FIRST_NAME)?.let {
                        record.get(BOOKING.NUM_OF_GUESTS)?.let { it1 ->
                            record.get(BOOKING.CHECK_IN_DATE)?.let { it2 ->
                                BookingDTO(
                                    bookingId = record.get(BOOKING.BOOKING_ID),
                                    userId = record.get(BOOKING.USER_ID),
                                    hotelId = record.get(BOOKING.HOTEL_ID),
                                    guestFirstName = it,
                                    guestLastName = record.get(BOOKING.GUEST_LAST_NAME)!!,
                                    numOfGuests = it1,
                                    occasion = record.get(BOOKING.OCCASION),
                                    guestNotes = record.get(BOOKING.GUEST_NOTES),
                                    checkInDate = it2,
                                    checkOutDate = record.get(BOOKING.CHECK_OUT_DATE)!!,
                                    totalCost = record.get(BOOKING.TOTAL_COST),
                                    roomDetails = record.get(ROOM.HOTEL_ID)?.let { it3 ->
                                        record.get(ROOM.ROOM_TYPE)?.let { it4 ->
                                            record.get(ROOM.ROOM_STATUS)?.let { it5 ->
                                                record.get(ROOM.BED_TYPE)?.let { it6 ->
                                                    record.get(ROOM.AVAILABILITY)?.let { it7 ->
                                                        RoomDTO(
                                                            roomId = record.get(ROOM.ROOM_ID),
                                                            hotelId = it3,
                                                            rate = record.get(RATE.RATE_)?.let { it4 ->
                                                                RateDTO(
                                                                    rateId = record.get(RATE.RATE_ID),
                                                                    rate = it4,
                                                                    month = record.get(RATE.MONTH)!!,
                                                                    defaultRate = record.get(RATE.DEFAULT_RATE)!!
                                                                )
                                                            },
                                                            roomNumber = record.get(ROOM.ROOM_NUMBER)!!,
                                                            roomType = it4,
                                                            roomStatus = it5,
                                                            bedType = it6,
                                                            maximumOccupancy = record.get(ROOM.MAXIMUM_OCCUPANCY)!!,
                                                            description = record.get(ROOM.DESCRIPTION)!!,
                                                            floor = record.get(ROOM.FLOOR)!!,
                                                            availability = it7,
                                                            lastCleaned = record.get(ROOM.LAST_CLEANED)
                                                                ?.toLocalDateTime(),
                                                            createdAt = record.get(ROOM.CREATED_AT)
                                                                ?.toLocalDateTime()!!,
                                                            updatedAt = record.get(ROOM.UPDATED_AT)
                                                                ?.toLocalDateTime()!!,
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    createdAt = record.get(BOOKING.CREATED_AT)?.toLocalDateTime()!!,
                                    updatedAt = record.get(BOOKING.UPDATED_AT)?.toLocalDateTime()!!,
                                )
                            }
                        }
                    }
                }



            return bookingsRecord
        } catch (e: Exception) {
            throw e
        }
    }


    fun getBookingsForAllHotels(): MutableList<BookingDTO> {
        try {
            val bookingsRecord = dslContext.select()
                .from(BOOKING)
                .join(ROOM).on(BOOKING.ROOM_ID.eq(ROOM.ROOM_ID))
                .join(RATE).on(ROOM.RATE_ID.eq(RATE.RATE_ID))
                .fetch { record ->
                    record.get(BOOKING.GUEST_FIRST_NAME)?.let {
                        record.get(BOOKING.NUM_OF_GUESTS)?.let { it1 ->
                            record.get(BOOKING.CHECK_IN_DATE)?.let { it2 ->
                                BookingDTO(
                                    bookingId = record.get(BOOKING.BOOKING_ID),
                                    userId = record.get(BOOKING.USER_ID),
                                    hotelId = record.get(BOOKING.HOTEL_ID),
                                    guestFirstName = it,
                                    guestLastName = record.get(BOOKING.GUEST_LAST_NAME)!!,
                                    numOfGuests = it1,
                                    occasion = record.get(BOOKING.OCCASION),
                                    guestNotes = record.get(BOOKING.GUEST_NOTES),
                                    checkInDate = it2,
                                    checkOutDate = record.get(BOOKING.CHECK_OUT_DATE)!!,
                                    totalCost = record.get(BOOKING.TOTAL_COST),
                                    roomDetails = record.get(ROOM.HOTEL_ID)?.let { it3 ->
                                        record.get(ROOM.ROOM_TYPE)?.let { it4 ->
                                            record.get(ROOM.ROOM_STATUS)?.let { it5 ->
                                                record.get(ROOM.BED_TYPE)?.let { it6 ->
                                                    record.get(ROOM.AVAILABILITY)?.let { it7 ->
                                                        RoomDTO(
                                                            roomId = record.get(ROOM.ROOM_ID),
                                                            hotelId = it3,
                                                            rate = record.get(RATE.RATE_)?.let { it4 ->
                                                                RateDTO(
                                                                    rateId = record.get(RATE.RATE_ID),
                                                                    rate = it4,
                                                                    month = record.get(RATE.MONTH)!!,
                                                                    defaultRate = record.get(RATE.DEFAULT_RATE)!!
                                                                )
                                                            },
                                                            roomNumber = record.get(ROOM.ROOM_NUMBER)!!,
                                                            roomType = it4,
                                                            roomStatus = it5,
                                                            bedType = it6,
                                                            maximumOccupancy = record.get(ROOM.MAXIMUM_OCCUPANCY)!!,
                                                            description = record.get(ROOM.DESCRIPTION)!!,
                                                            floor = record.get(ROOM.FLOOR)!!,
                                                            availability = it7,
                                                            lastCleaned = record.get(ROOM.LAST_CLEANED)
                                                                ?.toLocalDateTime(),
                                                            createdAt = record.get(ROOM.CREATED_AT)
                                                                ?.toLocalDateTime()!!,
                                                            updatedAt = record.get(ROOM.UPDATED_AT)
                                                                ?.toLocalDateTime()!!,
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    createdAt = record.get(BOOKING.CREATED_AT)?.toLocalDateTime()!!,
                                    updatedAt = record.get(BOOKING.UPDATED_AT)?.toLocalDateTime()!!,
                                )
                            }
                        }
                    }
                }



            return bookingsRecord
        } catch (e: Exception) {
            throw e
        }
    }


//    fun getAllAvailableHotels(): MutableList<HotelSearchDTO> {
//        try {
//            val availableHotels = dslContext.select()
//                .from(HOTEL)
//                .join(AMENITY).on(HOTEL.HOTEL_ID.eq(AMENITY.HOTEL_ID))
//                .join(ROOM).on(HOTEL.HOTEL_ID.eq(ROOM.HOTEL_ID))
//                .join(RATE).on(ROOM.RATE_ID.eq(RATE.RATE_ID))
//                .fetch { record ->
//                    record.get(HOTEL.HOTEL_ID)?.let {
//                        record.get(HOTEL.NAME)?.let { it1 ->
//                            record.get(ROOM.ROOM_TYPE)?.let { it2 ->
//                                record.get(ROOM.BED_TYPE)?.let { it3 ->
//                                    record.get(ROOM.AVAILABILITY)?.let { it4 ->
//                                        RoomSummarySearchDTO(
//                                            roomId = record.get(ROOM.ROOM_ID)!!,
//                                            roomType = it2,
//                                            bedType = it3,
//                                            availability = it4
//                                        )
//                                    }
//                                }
//                            }?.let { it3 ->
//                                record.get(RATE.RATE_)?.let { it2 ->
//                                    RateDTO(
//                                        rateId = record.get(ROOM.RATE_ID),
//                                        rate = it2,
//                                        month = record.get(RATE.MONTH)!!,
//                                        defaultRate = record.get(RATE.DEFAULT_RATE)!!
//                                    )
//                                }?.let { it4 ->
//                                    HotelSearchDTO(
//                                        hotelId = it,
//                                        name = it1,
//                                        amenities = AmenitySummarySearchDTO(
//                                            amenityId = record.get(AMENITY.AMENITY_ID)!!,
//                                            amenityName = record.get(AMENITY.AMENITY_NAME)!!
//                                        ),
//                                        roomSummary = it3,
//                                        rate = it4
//
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//
//            return availableHotels
//        }catch(e: Exception) {
//            e.printStackTrace()
//            throw e
//        }
//    }
fun getAllAvailableHotels(): List<HotelSearchDTO> {
    try {
        val availableHotels = dslContext.select()
            .from(HOTEL)
            .join(AMENITY).on(HOTEL.HOTEL_ID.eq(AMENITY.HOTEL_ID))
            .join(ROOM).on(HOTEL.HOTEL_ID.eq(ROOM.HOTEL_ID))
            .join(RATE).on(ROOM.RATE_ID.eq(RATE.RATE_ID))
            .join(LOCATION).on(HOTEL.LOCATION_ID.eq(LOCATION.LOCATION_ID))
            .where(ROOM.AVAILABILITY.eq(true))
            .fetchGroups(HOTEL.HOTEL_ID) { record ->
                val roomSummary = RoomSummarySearchDTO(
                    roomId = record.get(ROOM.ROOM_ID)!!,
                    roomType = record.get(ROOM.ROOM_TYPE)!!,
                    bedType = record.get(ROOM.BED_TYPE)!!,
                    availability = record.get(ROOM.AVAILABILITY)!!
                )

                val rateDTO = RateDTO(
                    rateId = record.get(RATE.RATE_ID)!!,
                    rate = record.get(RATE.RATE_)!!,
                    month = record.get(RATE.MONTH)!!,
                    defaultRate = record.get(RATE.DEFAULT_RATE)!!
                )

                val location = LocationSummarySearchDTO(
                    locationId = record.get(LOCATION.LOCATION_ID)!!,
                    city = record.get(LOCATION.CITY)!!,
                )
                HotelSearchDTO(
                    hotelId = record.get(HOTEL.HOTEL_ID)!!,
                    name = record.get(HOTEL.NAME)!!,
                    amenities = listOf(
                        AmenitySummarySearchDTO(
                            amenityId = record.get(AMENITY.AMENITY_ID)!!,
                            amenityName = record.get(AMENITY.AMENITY_NAME)!!
                        )
                    ),
                    roomSummary = roomSummary,
                    location = location,
                    rate = rateDTO
                )
            }

        return availableHotels.values.flatten()
    } catch (e: Exception) {
        e.printStackTrace()
        throw e
    }
}


}