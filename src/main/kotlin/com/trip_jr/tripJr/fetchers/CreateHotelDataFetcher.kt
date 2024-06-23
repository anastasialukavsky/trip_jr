package com.trip_jr.tripJr.fetchers

import com.trip_jr.tripJr.dto.hotel.AmenityDTO
import com.trip_jr.tripJr.dto.hotel.HotelDTO
import com.trip_jr.tripJr.dto.hotel.LocationDTO
import com.trip_jr.tripJr.service.aws.S3Service
import com.trip_jr.tripJr.service.hotel.HotelService
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Component
class CreateHotelDataFetcher(
    private val hotelService: HotelService,
    private val s3Service: S3Service
) : DataFetcher<HotelDTO> {

    override fun get(environment: DataFetchingEnvironment): HotelDTO? {
        val hotelInput = environment.getArgument<Map<String, Any>>("hotel")
        val files = environment.getArgument<List<MultipartFile>>("images")

        val hotel = convertToHotelDTO(hotelInput)
        val imageURLs = s3Service.uploadImages(files)

        return hotelService.createHotel(hotel)
    }

    private fun convertToHotelDTO(hotelInput: Map<String, Any>): HotelDTO {
        return HotelDTO(
            name = hotelInput["name"] as String,
            numOfRooms = (hotelInput["numOfRooms"] as Int).toInt(),
            description = hotelInput["description"] as String?,
            location = convertToLocationDTO(hotelInput["location"] as Map<String, Any>),
            amenities = (hotelInput["amenities"] as List<Map<String, Any>>).map { convertToAmenityDTO(it) }
        )
    }

    private fun convertToLocationDTO(locationInput: Map<String, Any>): LocationDTO {
        return LocationDTO(
            locationId = (locationInput["locationId"] as String?)?.let { UUID.fromString(it) },
            phoneNumber = locationInput["phoneNumber"] as String,
            address = locationInput["address"] as String,
            city = locationInput["city"] as String,
            state = locationInput["state"] as String,
            zip = locationInput["zip"] as String,
            latitude = (locationInput["latitude"] as Number).toDouble(),
            longitude = (locationInput["longitude"] as Number).toDouble()
        )
    }

    private fun convertToAmenityDTO(amenityInput: Map<String, Any>): AmenityDTO {
        return AmenityDTO(
            amenityId = (amenityInput["amenityId"] as String?)?.let { UUID.fromString(it) },
            amenityName = amenityInput["name"] as String,
            hotelId = (amenityInput["hotelId"] as String?)?.let { UUID.fromString(it) },
        )
    }
}