package com.trip_jr.tripJr.service.hotel

import com.trip_jr.tripJr.dto.hotel.HotelDTO
import com.trip_jr.tripJr.dto.hotel.LocationDTO
import com.trip_jr.tripJr.dto.hotel.RateDTO
import com.trip_jr.tripJr.jooq.tables.Hotel
import com.trip_jr.tripJr.jooq.tables.references.HOTEL
import com.trip_jr.tripJr.jooq.tables.references.LOCATION
import com.trip_jr.tripJr.jooq.tables.references.RATE
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import org.slf4j.LoggerFactory

@Service
class HotelService {

    @Autowired
    lateinit var dslContext: DSLContext

    private val logger = LoggerFactory.getLogger(HotelService::class.java)

    fun getAllHotels(): List<Hotel> {
        val hotels = dslContext.select().from(HOTEL).fetch()
        return hotels.map { hotel -> hotel.into(Hotel::class.java) }
    }

    fun getHotelById(id: UUID): HotelDTO {
        try {
            val record = dslContext.select()
                .from(HOTEL)
                .where(HOTEL.HOTEL_ID.eq(id))
                .fetchOne()

            val rates = dslContext.select()
                .from(RATE)
                .where(RATE.HOTEL_ID.eq(id))
                .fetchInto(RateDTO::class.java)

            return record?.let {
                val location = dslContext.select()
                    .from(LOCATION)
                    .where(LOCATION.LOCATION_ID.eq(it[HOTEL.LOCATION_ID]))
                    .fetchOneInto(LocationDTO::class.java)
                it[HOTEL.NAME]?.let { name ->
                    HotelDTO(
                        hotelId = it[HOTEL.HOTEL_ID],
                        name = name,
                        location = location ?: throw NoSuchElementException("Location not found"),
                        rates = rates
                    )
                } ?: throw NoSuchElementException("Hotel name not found")
            } ?: throw NoSuchElementException("Hotel with id $id not found")
        } catch (e: Exception) {
            throw e
        }
    }

    fun generateUniqueUUID(): UUID {
        return UUID.randomUUID()
    }

    fun createHotel(hotel: HotelDTO): HotelDTO? {
        try {

            val hotelId = generateUniqueUUID()

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
                .returningResult(HOTEL.HOTEL_ID)
                .fetchOne()

            if (hotelRecord == null) {
                throw Exception("Failed to create hotel")
            }


            val ratesRecords = hotel.rates.map { rate ->
                val rateId = rate.rateId ?: generateUniqueUUID()
                dslContext.insertInto(RATE)
                    .columns(RATE.RATE_ID, RATE.HOTEL_ID, RATE.RATE_, RATE.MONTH, RATE.DEFAULT_RATE)
                    .values(
                        rateId,
                        hotelId,
                        rate.rate,
                        rate.month,
                        rate.defaultRate
                    )
                    .execute()
                rate.copy(rateId = rateId, hotelId = hotelId)

            }

            return HotelDTO(
                hotelId = hotelRecord.get(HOTEL.HOTEL_ID),
                name = hotel.name,
                location = LocationDTO(
                    locationId = locationRecord?.get(LOCATION.LOCATION_ID),
                    phoneNumber = hotel.location.phoneNumber,
                    address = hotel.location.address,
                    city = hotel.location.city,
                    state = hotel.location.state,
                    zip = hotel.location.zip,
                    latitude = hotel.location.latitude,
                    longitude = hotel.location.longitude
                ),
                rates = ratesRecords

            )

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

}