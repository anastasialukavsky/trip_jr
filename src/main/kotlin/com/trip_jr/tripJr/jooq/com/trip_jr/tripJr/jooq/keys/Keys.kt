/*
 * This file is generated by jOOQ.
 */
package com.trip_jr.tripJr.jooq.keys


import com.trip_jr.tripJr.jooq.tables.Hotel
import com.trip_jr.tripJr.jooq.tables.Location
import com.trip_jr.tripJr.jooq.tables.Rate
import com.trip_jr.tripJr.jooq.tables.records.HotelRecord
import com.trip_jr.tripJr.jooq.tables.records.LocationRecord
import com.trip_jr.tripJr.jooq.tables.records.RateRecord

import org.jooq.ForeignKey
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal



// -------------------------------------------------------------------------
// UNIQUE and PRIMARY KEY definitions
// -------------------------------------------------------------------------

val HOTEL_LOCATION_ID_KEY: UniqueKey<HotelRecord> = Internal.createUniqueKey(Hotel.HOTEL, DSL.name("hotel_location_id_key"), arrayOf(Hotel.HOTEL.LOCATION_ID), true)
val HOTEL_PKEY: UniqueKey<HotelRecord> = Internal.createUniqueKey(Hotel.HOTEL, DSL.name("hotel_pkey"), arrayOf(Hotel.HOTEL.HOTEL_ID), true)
val LOCATION_PKEY: UniqueKey<LocationRecord> = Internal.createUniqueKey(Location.LOCATION, DSL.name("location_pkey"), arrayOf(Location.LOCATION.LOCATION_ID), true)
val RATE_PKEY: UniqueKey<RateRecord> = Internal.createUniqueKey(Rate.RATE, DSL.name("rate_pkey"), arrayOf(Rate.RATE.RATE_ID), true)

// -------------------------------------------------------------------------
// FOREIGN KEY definitions
// -------------------------------------------------------------------------

val HOTEL__FK_LOCATION: ForeignKey<HotelRecord, LocationRecord> = Internal.createForeignKey(Hotel.HOTEL, DSL.name("fk_location"), arrayOf(Hotel.HOTEL.LOCATION_ID), com.trip_jr.tripJr.jooq.keys.LOCATION_PKEY, arrayOf(Location.LOCATION.LOCATION_ID), true)
val RATE__FK_HOTEL: ForeignKey<RateRecord, HotelRecord> = Internal.createForeignKey(Rate.RATE, DSL.name("fk_hotel"), arrayOf(Rate.RATE.HOTEL_ID), com.trip_jr.tripJr.jooq.keys.HOTEL_PKEY, arrayOf(Hotel.HOTEL.HOTEL_ID), true)
val RATE__RATE_HOTEL_ID_FKEY: ForeignKey<RateRecord, HotelRecord> = Internal.createForeignKey(Rate.RATE, DSL.name("rate_hotel_id_fkey"), arrayOf(Rate.RATE.HOTEL_ID), com.trip_jr.tripJr.jooq.keys.HOTEL_PKEY, arrayOf(Hotel.HOTEL.HOTEL_ID), true)
