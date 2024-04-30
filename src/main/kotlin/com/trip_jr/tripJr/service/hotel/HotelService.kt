package com.trip_jr.tripJr.service.hotel

import com.trip_jr.tripJr.controller.review.ReviewController
import com.trip_jr.tripJr.dto.hotel.AmenityDTO
import com.trip_jr.tripJr.dto.hotel.HotelDTO
import com.trip_jr.tripJr.dto.hotel.LocationDTO
import com.trip_jr.tripJr.dto.hotel.RateDTO
import com.trip_jr.tripJr.dto.review.ReviewDTO
import com.trip_jr.tripJr.jooq.tables.Hotel
import com.trip_jr.tripJr.jooq.tables.references.*
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import org.slf4j.LoggerFactory

@Service
class HotelService {


    @Autowired
    lateinit var dslContext: DSLContext

    private val logger = LoggerFactory.getLogger(HotelService::class.java)


    fun getAllHotels(): List<HotelDTO> {
        val hotels = dslContext.select()
            .from(HOTEL)
            .join(LOCATION).on(HOTEL.LOCATION_ID.eq(LOCATION.LOCATION_ID))
            .join(RATE).on(RATE.HOTEL_ID.eq(HOTEL.HOTEL_ID))
            .join(AMENITY).on(AMENITY.HOTEL_ID.eq(HOTEL.HOTEL_ID))
            .fetch()

        return hotels.map { record ->
            val hotelId = record[HOTEL.HOTEL_ID]
            val name = record[HOTEL.NAME]
            val location = record[LOCATION.PHONE_NUMBER]?.let {
                record[LOCATION.ADDRESS]?.let { it1 ->
                    record[LOCATION.CITY]?.let { it2 ->
                        record[LOCATION.STATE]?.let { it3 ->
                            record[LOCATION.LATITUDE]?.let { it4 ->
                                record[LOCATION.LONGITUDE]?.let { it5 ->
                                    LocationDTO (
                                        locationId = record[LOCATION.LOCATION_ID],
                                        phoneNumber = it,
                                        address = it1,
                                        city = it2,
                                        state = it3,
                                        latitude = it4,
                                        longitude = it5
                                    )
                                }
                            }
                        }
                    }
                }
            }

            val rates = mutableListOf<RateDTO>()
            if (record[RATE.RATE_ID] != null && record[RATE.RATE_] != null && record[RATE.MONTH] != null && record[RATE.DEFAULT_RATE] != null) {
                val rate = record[RATE.RATE_]?.let {
                    record[RATE.MONTH]?.let { it1 ->
                        record[RATE.DEFAULT_RATE]?.let { it2 ->
                            RateDTO(
                                rateId = record[RATE.RATE_ID],
                                hotelId = record[RATE.HOTEL_ID],
                                rate = it,
                                month = it1,
                                defaultRate = it2,
                            )
                        }
                    }
                }
                if (rate != null) {
                    rates.add(rate)
                }
            }
            val amenities = mutableListOf<AmenityDTO>()
            if (record[AMENITY.AMENITY_ID] != null && record[AMENITY.AMENITY_NAME] != null) {
                val amenity = record[AMENITY.AMENITY_NAME]?.let {
                    AmenityDTO(
                        amenityId = record[AMENITY.AMENITY_ID],
                        amenityName = it,
                        hotelId = record[AMENITY.HOTEL_ID],
                    )
                }
                if (amenity != null) {
                    amenities.add(amenity)
                }
            }


            HotelDTO(hotelId, name!!, location!!, rates, amenities)
        }
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

            val amenities = dslContext.select()
                .from(AMENITY)
                .where(AMENITY.HOTEL_ID.eq(id))
                .fetchInto(AmenityDTO::class.java)

            val location = record?.let {
                dslContext.select()
                    .from(LOCATION)
                    .where(LOCATION.LOCATION_ID.eq(it[HOTEL.LOCATION_ID]))
                    .fetchOneInto(LocationDTO::class.java)
            } ?: throw NoSuchElementException("Location not found")

            // Fetch reviews associated with the hotel
            val reviews = dslContext.select()
                .from(REVIEW)
                .where(REVIEW.HOTEL_ID.eq(id))
                .fetchInto(ReviewDTO::class.java)

            return record.let {
                it[HOTEL.NAME]?.let { name ->
                    HotelDTO(
                        hotelId = it[HOTEL.HOTEL_ID],
                        name = name,
                        location = location,
                        rates = rates,
                        amenities = amenities,
                        reviews = reviews
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
            val locationId = generateUniqueUUID()
            logger.debug("LOC ID", locationId)


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

            logger.debug("LOCATION RECORD", locationRecord)

            logger.debug("LOC REC ", locationRecord?.get(LOCATION.LOCATION_ID))
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

            val amenitiesRecords = hotel.amenities.map { amenity ->
                val amenityId = amenity.amenityId ?: generateUniqueUUID()
                dslContext.insertInto(AMENITY)
                    .columns(AMENITY.AMENITY_ID, AMENITY.AMENITY_NAME, AMENITY.HOTEL_ID)
                    .values(
                        amenityId,
                        amenity.amenityName,
                        hotelId
                    )
                    .execute()
                amenity.copy(amenityId = amenityId, hotelId = hotelId)
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
                rates = ratesRecords,
                amenities = amenitiesRecords

            )

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

}