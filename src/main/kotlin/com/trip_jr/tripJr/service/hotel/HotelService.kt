package com.trip_jr.tripJr.service.hotel

import com.trip_jr.tripJr.dto.hotel.HotelDTO
import com.trip_jr.tripJr.jooq.tables.Hotel
import com.trip_jr.tripJr.jooq.tables.references.HOTEL
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.jooq.impl.DSL.table
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class HotelService {

    @Autowired
    lateinit var dslContext: DSLContext

//    fun getHotelById(id: UUID): Hotel? {
//        val hotel = dslContext.select().from(HOTEL).where(id)
//    }

    fun getAllHotels(): List<Hotel> {
        val hotels = dslContext.select().from(HOTEL).fetch()
        return hotels.map { hotel -> hotel.into(Hotel::class.java) }
    }

    fun createHotel(hotel: HotelDTO): HotelDTO {
        val newHotel = dslContext.insertInto(table("hotel"))
            .set(HOTEL.HOTEL_ID, UUID.randomUUID())
            .set(HOTEL.NAME, hotel.name)
            .returning()
            .fetchOne()

        return newHotel?.into(HotelDTO::class.java) ?: throw IllegalStateException("Failed to insert hotel record")
    }
}