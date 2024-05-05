package com.trip_jr.tripJr.service.hotel

import com.trip_jr.tripJr.dto.booking.BookingDTO
import com.trip_jr.tripJr.dto.hotel.AmenityDTO
import com.trip_jr.tripJr.dto.hotel.HotelDTO
import com.trip_jr.tripJr.dto.hotel.LocationDTO
import com.trip_jr.tripJr.dto.hotel.RateDTO
import com.trip_jr.tripJr.dto.review.ReviewDTO
import com.trip_jr.tripJr.jooq.tables.references.*
import com.trip_jr.tripJr.service.utils.HotelByIdUitls
import com.trip_jr.tripJr.service.utils.UUIDUtils
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import org.slf4j.LoggerFactory

@Service
class HotelService {


    @Autowired
    lateinit var dslContext: DSLContext

    @Autowired
    lateinit var hotelByIdUtils: HotelByIdUitls

    @Autowired
    lateinit var uuidUtils: UUIDUtils

    private val logger = LoggerFactory.getLogger(HotelService::class.java)


    fun getAllHotels(): List<HotelDTO> {
        val hotels = dslContext
            .select()
            .from(HOTEL)
            .join(LOCATION).on(HOTEL.LOCATION_ID.eq(LOCATION.LOCATION_ID))
            .join(RATE).on(RATE.HOTEL_ID.eq(HOTEL.HOTEL_ID))
            .join(AMENITY).on(AMENITY.HOTEL_ID.eq(HOTEL.HOTEL_ID))
            .join(REVIEW).on(REVIEW.HOTEL_ID.eq(REVIEW.HOTEL_ID))
            .join(BOOKING).on(BOOKING.HOTEL_ID.eq(BOOKING.HOTEL_ID))
            .fetch()


        return hotels.map { record ->
            val hotelId = record[HOTEL.HOTEL_ID]
            val name = record[HOTEL.NAME]
            val numOfRooms = record[HOTEL.NUM_OF_ROOMS] ?: 1
            val description = record[HOTEL.DESCRIPTION] ?: ""
            val location = record[LOCATION.PHONE_NUMBER]?.let {
                record[LOCATION.ADDRESS]?.let { it1 ->
                    record[LOCATION.CITY]?.let { it2 ->
                        record[LOCATION.STATE]?.let { it3 ->
                            record[LOCATION.LATITUDE]?.let { it4 ->
                                record[LOCATION.LONGITUDE]?.let { it5 ->
                                    record[LOCATION.ZIP]?.let { it6 ->
                                        LocationDTO(
                                            locationId = record[LOCATION.LOCATION_ID],
                                            phoneNumber = it,
                                            address = it1,
                                            city = it2,
                                            state = it3,
                                            zip = it6,
                                            latitude = it4,
                                            longitude = it5
                                        )
                                    }
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


            val reviews = mutableListOf<ReviewDTO>()
            val review = record[REVIEW.RATING]?.let {
                record[REVIEW.REVIEW_TITLE]?.let { it1 ->
                    record[REVIEW.REVIEW_BODY]?.let { it2 ->
                        ReviewDTO(
                            reviewId = record[REVIEW.REVIEW_ID],
                            userId = record[REVIEW.USER_ID],
                            hotelId = record[REVIEW.HOTEL_ID],
                            rating = it,
                            reviewTitle = it1,
                            reviewBody = it2
                        )
                    }
                }
            }
            if (review != null) {
                reviews.add(review)
            }

            val bookings = mutableListOf<BookingDTO>()
            val booking = record[BOOKING.CHECK_IN_DATE]?.let {
                record[BOOKING.CHECK_OUT_DATE]?.let { it1 ->
                    record[BOOKING.GUEST_FIRST_NAME]?.let { it2 ->
                        record[BOOKING.GUEST_LAST_NAME]?.let { it3 ->
                            record[BOOKING.NUM_OF_GUESTS]?.let { it4 ->
                                record[BOOKING.CREATED_AT]?.toLocalDateTime()?.let { it5 ->
                                    record[BOOKING.UPDATED_AT]?.toLocalDateTime()?.let { it6 ->
                                        BookingDTO(
                                            bookingId = record[BOOKING.BOOKING_ID],
                                            userId = record[BOOKING.USER_ID],
                                            hotelId = record[BOOKING.HOTEL_ID],
                                            guestFirstName = it2,
                                            guestLastName = it3,
                                            numOfGuests = it4,
                                            occasion = record[BOOKING.OCCASION],
                                            guestNotes = record[BOOKING.GUEST_NOTES],
                                            checkInDate = it,
                                            checkOutDate = it1,
                                            totalCost = record[BOOKING.TOTAL_COST],
                                            createdAt = it5,
                                            updatedAt = it6
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (booking != null) {
                bookings.add(booking)
            }


            HotelDTO(hotelId, name!!, numOfRooms, description, location!!, rates, amenities, reviews, bookings)
        }
    }

    fun getHotelById(id: UUID): HotelDTO {
        try {
            val record = dslContext.select()
                .from(HOTEL)
                .where(HOTEL.HOTEL_ID.eq(id))
                .fetchOne()

            val rates = hotelByIdUtils.getHotelRates(id)
            val amenities = hotelByIdUtils.getHotelAmenities(id)
            val reviews = hotelByIdUtils.getHotelReviews(id)
            val bookings = hotelByIdUtils.getHotelBookings(id)


            val location = record?.let {
                dslContext.select()
                    .from(LOCATION)
                    .where(LOCATION.LOCATION_ID.eq(it[HOTEL.LOCATION_ID]))
                    .fetchOneInto(LocationDTO::class.java)
            } ?: throw NoSuchElementException("Location not found")


            return HotelDTO(
                hotelId = record[HOTEL.HOTEL_ID],
                name = record[HOTEL.NAME] ?: "",
                numOfRooms = record[HOTEL.NUM_OF_ROOMS] ?: 1,
                description = record[HOTEL.DESCRIPTION] ?: "",
                location = location,
                rates = rates,
                amenities = amenities,
                reviews = reviews,
                bookings = bookings
            )


        } catch (e: Exception) {
            throw e
        }
    }


    fun createHotel(hotel: HotelDTO): HotelDTO? {
        try {
            val hotelId = uuidUtils.generateUUID()
            val locationId = uuidUtils.generateUUID()

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
                    hotel.location.locationId ?: locationId,
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
                .columns(HOTEL.HOTEL_ID, HOTEL.NAME, HOTEL.NUM_OF_ROOMS, HOTEL.DESCRIPTION, HOTEL.LOCATION_ID)
                .values(hotelId, hotel.name, hotel.numOfRooms, hotel.description, locationRecord?.get(LOCATION.LOCATION_ID))
                .returningResult(HOTEL.HOTEL_ID)
                .fetchOne()

            if (hotelRecord == null) {
                throw Exception("Failed to create hotel")
            }

            val ratesRecords = hotel.rates.map { rate ->
                val rateId = rate.rateId ?: uuidUtils.generateUUID()
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
                val amenityId = amenity.amenityId ?: uuidUtils.generateUUID()
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

            val locationDTO = LocationDTO(
                locationId = locationRecord?.get(LOCATION.LOCATION_ID),
                phoneNumber = hotel.location.phoneNumber,
                address = hotel.location.address,
                city = hotel.location.city,
                state = hotel.location.state,
                zip = hotel.location.zip,
                latitude = hotel.location.latitude,
                longitude = hotel.location.longitude
            )

            return HotelDTO(
                hotelId = hotelId,
                name = hotel.name ?: "",
                numOfRooms = hotel.numOfRooms,
                description = hotel.description,
                location = locationDTO,
                rates = ratesRecords,
                amenities = amenitiesRecords
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }


}