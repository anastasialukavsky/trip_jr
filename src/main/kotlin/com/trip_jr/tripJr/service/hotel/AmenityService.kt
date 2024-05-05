package com.trip_jr.tripJr.service.hotel

import com.trip_jr.tripJr.dto.hotel.AmenityDTO
import com.trip_jr.tripJr.dto.hotel.updateDTOs.UpdateAmenityDTO
import com.trip_jr.tripJr.jooq.tables.references.AMENITY
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*


@Service
class AmenityService {

    @Autowired
    lateinit var dslContext: DSLContext

    fun updateHotelAmenity(hotelId: UUID, amenityId: UUID, amenity: UpdateAmenityDTO): AmenityDTO? {
        try {
        val amenityRecord = dslContext.select()
            .from(AMENITY)
            .where(AMENITY.AMENITY_ID.eq(amenityId))
            .and(AMENITY.HOTEL_ID.eq(hotelId))
            .fetchOne() ?: throw RuntimeException("Amenity with ID $amenityId not found")

            val currentTimestamp = OffsetDateTime.now(ZoneOffset.UTC)

            val originalAmenityRecord: AmenityDTO? = amenityRecord.get(AMENITY.CREATED_AT)?.toLocalDateTime()?.let {
                amenityRecord.get(AMENITY.AMENITY_NAME)?.let { it1 ->
                    AmenityDTO(
                        amenityId = amenityId,
                        amenityName = it1,
                        hotelId = hotelId,
                        createdAt = it
                    )
                }
            }

            val updatedAmenityRecord = originalAmenityRecord?.copy(
                amenityName = amenity.amenityName ?: originalAmenityRecord.amenityName,
                createdAt = originalAmenityRecord.createdAt,
                updatedAt = currentTimestamp.toLocalDateTime()
            )

            val updateQuery = dslContext.update(AMENITY)
                .set(AMENITY.AMENITY_NAME, updatedAmenityRecord?.amenityName)
                .set(AMENITY.UPDATED_AT, currentTimestamp)
                .where(AMENITY.AMENITY_ID.eq(amenityId))
                .and(AMENITY.HOTEL_ID.eq(hotelId))
                .execute()

            return if(updateQuery == 1) updatedAmenityRecord
            else throw RuntimeException("Failed to update amenity")


        } catch (e: Exception) {
            throw e
        }
    }
}