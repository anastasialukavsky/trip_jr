package com.trip_jr.tripJr.service.hotel

import com.trip_jr.tripJr.dto.booking.BookingDTO
import com.trip_jr.tripJr.dto.hotel.*
import com.trip_jr.tripJr.dto.hotel.updateDTOs.UpdateHotelDTO
import com.trip_jr.tripJr.dto.review.ReviewDTO
import com.trip_jr.tripJr.jooq.tables.Amenity
import com.trip_jr.tripJr.jooq.tables.Hotel
import com.trip_jr.tripJr.jooq.tables.references.*
import com.trip_jr.tripJr.repository.hotel.HotelRepository
import com.trip_jr.tripJr.service.aws.S3Service
import com.trip_jr.tripJr.service.utils.HotelByIdUtils
import com.trip_jr.tripJr.service.utils.UUIDUtils
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import org.slf4j.LoggerFactory
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Service
class HotelService {


    @Autowired
    private lateinit var hotelRepository: HotelRepository

    @Autowired
    lateinit var dslContext: DSLContext

    @Autowired
    lateinit var hotelByIdUtils: HotelByIdUtils

    @Autowired
    lateinit var uuidUtils: UUIDUtils

//    @Autowired
//    lateinit var s3Client: S3Client
//
//    @Autowired
//    lateinit var s3Presigner: S3Presigner

    private val logger = LoggerFactory.getLogger(HotelService::class.java)


    fun getAllHotels(): List<HotelDTO> {
        val hotels = dslContext
            .select()
            .from(HOTEL)
            .join(LOCATION).on(HOTEL.LOCATION_ID.eq(LOCATION.LOCATION_ID))
            .join(AMENITY).on(AMENITY.HOTEL_ID.eq(HOTEL.HOTEL_ID))
            .join(REVIEW).on(REVIEW.HOTEL_ID.eq(REVIEW.HOTEL_ID))
//            .join(BOOKING).on(BOOKING.HOTEL_ID.eq(BOOKING.HOTEL_ID))
//            .join(ROOM).on(HOTEL.HOTEL_ID.eq(ROOM.HOTEL_ID))
//            .join(RATE).on(ROOM.RATE_ID.eq(RATE.RATE_ID))
            .fetch()


      val hotelDTOList =    hotels.map { record ->
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

            val id = record.get(HOTEL.HOTEL_ID)
//            val bookings = id?.let { hotelRepository.getHotelBookings(it) }

          val bookings = hotelRepository.getBookingsForAllHotels()
            HotelDTO(hotelId = hotelId, name = name!!, numOfRooms = numOfRooms, description = description, location =location!!, amenities = amenities, reviews = reviews, bookings = bookings!!)
        }
        return hotelDTOList
    }


    fun getHotelById(id: UUID): HotelDTO? {
        try {
            val record = dslContext.select()
                .from(HOTEL)
                .join(LOCATION).on(HOTEL.LOCATION_ID.eq(LOCATION.LOCATION_ID))
                .where(HOTEL.HOTEL_ID.eq(id))
                .fetchOne()

            val amenities = hotelByIdUtils.getHotelAmenities(id)
            val reviews = hotelByIdUtils.getHotelReviews(id)
            val bookings = hotelRepository.getHotelBookings(id)
            val location = record?.into(LocationDTO::class.java)

            return location?.let {
                HotelDTO(
                    hotelId = record.get(HOTEL.HOTEL_ID),
                    name = record.get(HOTEL.NAME) ?: "",
                    numOfRooms = record.get(HOTEL.NUM_OF_ROOMS) ?: 1,
                    description = record.get(HOTEL.DESCRIPTION) ?: "",
                    location = it,
                    amenities = amenities,
                    reviews = reviews,
                    bookings = bookings
                )
            }

        } catch (e: Exception) {
            throw e
        }
    }


    fun createHotel(hotel: HotelDTO, imageURLs: List<String>): HotelDTO? {
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
                .columns(HOTEL.HOTEL_ID, HOTEL.NAME, HOTEL.NUM_OF_ROOMS, HOTEL.DESCRIPTION, HOTEL.HOTEL_IMAGES, HOTEL.LOCATION_ID)
                .values(
                    hotelId,
                    hotel.name,
                    hotel.numOfRooms,
                    hotel.description,
                    hotel.hotelImageURLs?.joinToString(","),
                    locationRecord?.get(LOCATION.LOCATION_ID)
                )
                .returningResult(HOTEL.HOTEL_ID)
                .fetchOne()

            if (hotelRecord == null) {
                throw Exception("Failed to create hotel")
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


            return hotel.copy(
                hotelId = hotelId,
                location = locationDTO,
                hotelImageURLs = imageURLs,
                amenities = amenitiesRecords
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }


    fun updateHotel(id: UUID, hotel: UpdateHotelDTO): HotelDTO? {
        try {
            val hotelRecord = dslContext
                .select()
                .from(HOTEL)
                .join(LOCATION).on(HOTEL.LOCATION_ID.eq(LOCATION.LOCATION_ID))
                .where(HOTEL.HOTEL_ID.eq(id))
                .fetchOne() ?: throw RuntimeException("Hotel with ID $id not found")

//            val rates = hotelByIdUtils.getHotelRates(id)
            val amenities = hotelByIdUtils.getHotelAmenities(id)
            val reviews = hotelByIdUtils.getHotelReviews(id)
            val bookings = hotelByIdUtils.getHotelBookings(id)


            val location = hotelRecord.into(LocationDTO::class.java)

            val currentTimestamp = OffsetDateTime.now(ZoneOffset.UTC)

            val originalHotelRecord: HotelDTO? =
                hotelRecord.get(HOTEL.CREATED_AT)?.let {
                    hotelRecord.get(HOTEL.NAME)?.let { it1 ->
                        HotelDTO(
                            hotelId = id,
                            name = it1,
                            numOfRooms = hotelRecord.get(HOTEL.NUM_OF_ROOMS),
                            description = hotelRecord.get(HOTEL.DESCRIPTION),
                            location = location,
//                            rates = rates,
                            amenities = amenities,
                            reviews = reviews,
                            bookings = bookings,
                            createdAt = it.toLocalDateTime(),
                            updatedAt = currentTimestamp.toLocalDateTime()
                        )
                    }
                }

            val updatedHotelRecord = originalHotelRecord?.copy(
                name = hotel.name ?: originalHotelRecord.name,
                numOfRooms = hotel.numOfRooms ?: originalHotelRecord.numOfRooms,
                description = hotel.description ?: originalHotelRecord.description,
                createdAt = originalHotelRecord.createdAt,
                updatedAt = originalHotelRecord.updatedAt,
                location = location,
//                rates = rates,
                amenities = amenities,
                reviews = reviews,
                bookings = bookings
            )

            val updateQuery = dslContext.update(HOTEL)
                .set(HOTEL.NAME, updatedHotelRecord?.name)
                .set(HOTEL.NUM_OF_ROOMS, updatedHotelRecord?.numOfRooms)
                .set(HOTEL.DESCRIPTION, updatedHotelRecord?.description)
//                .set(HOTEL.CREATED_AT, updatedHotelRecord?.createdAt)
                .set(HOTEL.UPDATED_AT, currentTimestamp)
                .where(HOTEL.HOTEL_ID.eq(id))
                .execute()

            return if (updateQuery == 1) updatedHotelRecord
            else throw RuntimeException("Failed to update hotel record")


        } catch (e: Exception) {
            throw e
        }
    }


    fun deleteHotel(id: UUID): Boolean {
        try {
            var isDeleteSuccessful = false
            dslContext.transaction { config ->
                dslContext.deleteFrom(BOOKING)
                    .where(BOOKING.HOTEL_ID.eq(id))
                    .execute()

                dslContext.deleteFrom(REVIEW)
                    .where(REVIEW.HOTEL_ID.eq(id))
                    .execute()

                dslContext.deleteFrom(AMENITY)
                    .where(AMENITY.HOTEL_ID.eq(id))
                    .execute()

//                dslContext.deleteFrom(RATE)
//                    .where(RATE.HOTEL_ID.eq(id))
//                    .execute()

//                val locationId = dslContext.select(HOTEL.LOCATION_ID)
//                    .from(HOTEL)
//                    .where(HOTEL.HOTEL_ID.eq(id))
//                    .fetchOne(HOTEL.LOCATION_ID) ?: throw RuntimeException("Location with ID $locationId not found")
//
//                dslContext.deleteFrom(LOCATION)
//                    .where(LOCATION.LOCATION_ID.eq(locationId))
//                    .execute()

                val deletedHotelCount = dslContext.deleteFrom(HOTEL)
                    .where(HOTEL.HOTEL_ID.eq(id))
                    .execute()

                isDeleteSuccessful = deletedHotelCount == 1
            }

            return isDeleteSuccessful
        } catch (e: Exception) {
            throw e
        }
    }
}

//
//{
//    "query": "mutation($hotel: HotelInput!, $hotelImageURLs: [Upload] { createHotel(hotel: $hotel, imageURLs: $hotelImageURLs) {hotelId, name, hotelImageURLs: $hotelImageURLs}) }",
//    "variables" : {
//        "hotel": {
//            "name" : "Pic upload test",
//            "numOfRooms" : 10,
//            "description" : "I hope this works",
//            "hotelImageURLs" : [null, null],
//        "location" : {
//            "phoneNumber": "1234567890",
//            "address": "123 Test street",
//            "city": "Test City",
//            "state": "TS",
//            "zip": "11223",
//            "latitude": 12.34,
//            "longitude": 12.34
//},
//    "amenities": [
//    {
//        "amenityName" : "free wifi"
//    }
//    ]
//}
//}
//}