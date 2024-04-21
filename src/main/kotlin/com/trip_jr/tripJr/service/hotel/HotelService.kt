package com.trip_jr.tripJr.service.hotel

import com.trip_jr.tripJr.dto.hotel.HotelDTO
import com.trip_jr.tripJr.dto.hotel.LocationDTO
import com.trip_jr.tripJr.jooq.tables.Hotel
import com.trip_jr.tripJr.jooq.tables.references.HOTEL
import com.trip_jr.tripJr.jooq.tables.references.LOCATION
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.table
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import org.slf4j.LoggerFactory

@Service
class HotelService {

    @Autowired
    lateinit var dslContext: DSLContext


    private val logger = LoggerFactory.getLogger(HotelService::class.java)
//    fun getHotelById(id: UUID): Hotel? {
//        val hotel = dslContext.select().from(HOTEL).where(id)
//    }

    fun getAllHotels(): List<Hotel> {
        val hotels = dslContext.select().from(HOTEL).fetch()
        return hotels.map { hotel -> hotel.into(Hotel::class.java) }
    }


fun createHotel(hotel: HotelDTO): HotelDTO? {
    val hotelId = hotel.hotelId ?: UUID.randomUUID()


    val locationRecord = dslContext.insertInto(LOCATION)
        .columns(
            LOCATION.LOCATION_ID,
            LOCATION.PHONE_NUMBER,
            LOCATION.ADDRESS,
            LOCATION.CITY,
            LOCATION.STATE,
            LOCATION.ZIP,
            LOCATION.LATITUDE,
            LOCATION.LONGITUDE
        )
        .values(
            hotel.location.locationId ?: UUID.randomUUID(),
            hotel.location.phoneNumber,
            hotel.location.address,
            hotel.location.city,
            hotel.location.state,
            hotel.location.zip,
            hotel.location.latitude,
            hotel.location.longitude
        )
        .returningResult(LOCATION.LOCATION_ID)
        .fetchOne()

    val hotelRecord = dslContext.insertInto(HOTEL)
        .columns(HOTEL.HOTEL_ID, HOTEL.NAME, HOTEL.LOCATION_ID)
        .values(hotelId, hotel.name, locationRecord?.get(LOCATION.LOCATION_ID))
        .returningResult(HOTEL.HOTEL_ID, HOTEL.NAME)
        .fetchOne()

    return if (hotelRecord != null && locationRecord != null) {
        hotelRecord.get(HOTEL.NAME)?.let {
            HotelDTO(
                hotelId = hotelRecord.get(HOTEL.HOTEL_ID),
                name = it,
                location = LocationDTO(
                    locationId = locationRecord.get(LOCATION.LOCATION_ID),
                    phoneNumber = hotel.location.phoneNumber,
                    address = hotel.location.address,
                    city = hotel.location.city,
                    state = hotel.location.state,
                    zip = hotel.location.zip,
                    latitude = hotel.location.latitude,
                    longitude = hotel.location.longitude
                )
            )
        }
    } else {
        null
    }
}

}