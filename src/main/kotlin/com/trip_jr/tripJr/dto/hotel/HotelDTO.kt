package com.trip_jr.tripJr.dto.hotel

import java.util.UUID

data class HotelDTO(
    val hotelId: UUID? =null,
    val name: String = "",
    var location: LocationDTO,
    val rates: List<RateDTO> = listOf()
)
































/**
 *   fun createHotel(hotel: HotelDTO): HotelDTO? {
 *         val hotelId = hotel.hotelId ?: UUID.randomUUID()
 *
 *         // Insert into hotel table
 *         val hotelRecord = dslContext.insertInto(HOTEL)
 *             .columns(HOTEL.HOTEL_ID, HOTEL.NAME)
 *             .values(hotelId, hotel.name)
 *             .returningResult(HOTEL.HOTEL_ID, HOTEL.NAME)
 *             .fetchOne()
 *
 *         return if (hotelRecord != null) {
 *             hotelRecord.get(HOTEL.NAME)?.let {
 *                 HotelDTO(
 *                     hotelId = hotelRecord.get(HOTEL.HOTEL_ID),
 *                     name = it
 *                 )
 *             }
 *         } else {
 *             null // Return null if no hotel record is inserted
 *         }
 *     }*/