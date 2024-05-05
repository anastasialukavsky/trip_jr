package com.trip_jr.tripJr.service.hotel

import com.trip_jr.tripJr.dto.hotel.LocationDTO
import com.trip_jr.tripJr.dto.hotel.updateDTOs.UpdateLocationDTO
import com.trip_jr.tripJr.jooq.keys.LOCATION_PKEY
import com.trip_jr.tripJr.jooq.tables.references.LOCATION
import org.slf4j.LoggerFactory
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@Service
class LocationService {

    @Autowired
    lateinit var dslContext: DSLContext

    private val logger = LoggerFactory.getLogger(LocationService::class.java)

    fun updateLocation(id: UUID, location: UpdateLocationDTO): LocationDTO? {
        try {
            val locationRecord = dslContext.select()
                .from(LOCATION)
                .where(LOCATION.LOCATION_ID.eq(id))
                .fetchOne() ?: throw RuntimeException("Location with ID $id not found")

            val currentTimestamp = OffsetDateTime.now(ZoneOffset.UTC)

            val originalLocationRecord: LocationDTO? =
                locationRecord.get(LOCATION.PHONE_NUMBER)?.let {
                    locationRecord.get(LOCATION.LATITUDE)?.let { it1 ->
                        locationRecord.get(LOCATION.CREATED_AT)?.toLocalDateTime()?.let { it2 ->
                            LocationDTO(
                                locationId = id,
                                phoneNumber = it,
                                address = locationRecord.get(LOCATION.ADDRESS)!!,
                                city = locationRecord.get(LOCATION.CITY)!!,
                                state = locationRecord.get(LOCATION.STATE)!!,
                                zip = locationRecord.get(LOCATION.ZIP)!!,
                                latitude = it1,
                                longitude = locationRecord.get(LOCATION.LONGITUDE)!!,
                                createdAt = it2,
                                updatedAt = currentTimestamp.toLocalDateTime()
                            )
                        }
                    }
                }

            val updatedLocationRecord = originalLocationRecord?.copy(
                phoneNumber = location.phoneNumber ?: originalLocationRecord.phoneNumber,
                address = location.address ?: originalLocationRecord.address,
                city = location.city ?: originalLocationRecord.city,
                state = location.state ?: originalLocationRecord.state,
                zip = location.zip ?: originalLocationRecord.zip,
                latitude = location.latitude ?: originalLocationRecord.latitude,
                longitude = location.longitude ?: originalLocationRecord.longitude,
                createdAt = originalLocationRecord.createdAt,
                updatedAt = originalLocationRecord.createdAt
            )

            val updateQuery = dslContext.update(LOCATION)
                .set(LOCATION.PHONE_NUMBER, updatedLocationRecord?.phoneNumber)
                .set(LOCATION.ADDRESS, updatedLocationRecord?.address)
                .set(LOCATION.CITY, updatedLocationRecord?.city)
                .set(LOCATION.STATE, updatedLocationRecord?.state)
                .set(LOCATION.ZIP, updatedLocationRecord?.zip)
                .set(LOCATION.LATITUDE, updatedLocationRecord?.latitude)
                .set(LOCATION.LONGITUDE, updatedLocationRecord?.longitude)
                .set(LOCATION.UPDATED_AT, currentTimestamp)
                .where(LOCATION.LOCATION_ID.eq(id))
                .execute()


            return if (updateQuery == 1) updatedLocationRecord
             else throw RuntimeException("Failed to update location")

        } catch (e: Exception) {
            throw e
        }
    }
}