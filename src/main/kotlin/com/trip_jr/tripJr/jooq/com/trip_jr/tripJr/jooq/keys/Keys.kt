/*
 * This file is generated by jOOQ.
 */
package com.trip_jr.tripJr.jooq.keys


import com.trip_jr.tripJr.jooq.tables.Amenity
import com.trip_jr.tripJr.jooq.tables.Booking
import com.trip_jr.tripJr.jooq.tables.Hotel
import com.trip_jr.tripJr.jooq.tables.HotelClaim
import com.trip_jr.tripJr.jooq.tables.Location
import com.trip_jr.tripJr.jooq.tables.Rate
import com.trip_jr.tripJr.jooq.tables.Review
import com.trip_jr.tripJr.jooq.tables.Room
import com.trip_jr.tripJr.jooq.tables.Users
import com.trip_jr.tripJr.jooq.tables.records.AmenityRecord
import com.trip_jr.tripJr.jooq.tables.records.BookingRecord
import com.trip_jr.tripJr.jooq.tables.records.HotelClaimRecord
import com.trip_jr.tripJr.jooq.tables.records.HotelRecord
import com.trip_jr.tripJr.jooq.tables.records.LocationRecord
import com.trip_jr.tripJr.jooq.tables.records.RateRecord
import com.trip_jr.tripJr.jooq.tables.records.ReviewRecord
import com.trip_jr.tripJr.jooq.tables.records.RoomRecord
import com.trip_jr.tripJr.jooq.tables.records.UsersRecord

import org.jooq.ForeignKey
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal



// -------------------------------------------------------------------------
// UNIQUE and PRIMARY KEY definitions
// -------------------------------------------------------------------------

val AMENITY_PKEY: UniqueKey<AmenityRecord> = Internal.createUniqueKey(Amenity.AMENITY, DSL.name("amenity_pkey"), arrayOf(Amenity.AMENITY.AMENITY_ID), true)
val BOOKING_PKEY: UniqueKey<BookingRecord> = Internal.createUniqueKey(Booking.BOOKING, DSL.name("booking_pkey"), arrayOf(Booking.BOOKING.BOOKING_ID), true)
val HOTEL_LOCATION_ID_KEY: UniqueKey<HotelRecord> = Internal.createUniqueKey(Hotel.HOTEL, DSL.name("hotel_location_id_key"), arrayOf(Hotel.HOTEL.LOCATION_ID), true)
val HOTEL_PKEY: UniqueKey<HotelRecord> = Internal.createUniqueKey(Hotel.HOTEL, DSL.name("hotel_pkey"), arrayOf(Hotel.HOTEL.HOTEL_ID), true)
val HOTEL_CLAIMS_PKEY: UniqueKey<HotelClaimRecord> = Internal.createUniqueKey(HotelClaim.HOTEL_CLAIM, DSL.name("hotel_claims_pkey"), arrayOf(HotelClaim.HOTEL_CLAIM.CLAIM_ID), true)
val LOCATION_PKEY: UniqueKey<LocationRecord> = Internal.createUniqueKey(Location.LOCATION, DSL.name("location_pkey"), arrayOf(Location.LOCATION.LOCATION_ID), true)
val RATE_PKEY: UniqueKey<RateRecord> = Internal.createUniqueKey(Rate.RATE, DSL.name("rate_pkey"), arrayOf(Rate.RATE.RATE_ID), true)
val REVIEW_PKEY: UniqueKey<ReviewRecord> = Internal.createUniqueKey(Review.REVIEW, DSL.name("review_pkey"), arrayOf(Review.REVIEW.REVIEW_ID), true)
val ROOM_PKEY: UniqueKey<RoomRecord> = Internal.createUniqueKey(Room.ROOM, DSL.name("room_pkey"), arrayOf(Room.ROOM.ROOM_ID), true)
val USERS_EMAIL_KEY: UniqueKey<UsersRecord> = Internal.createUniqueKey(Users.USERS, DSL.name("users_email_key"), arrayOf(Users.USERS.EMAIL), true)
val USERS_PKEY: UniqueKey<UsersRecord> = Internal.createUniqueKey(Users.USERS, DSL.name("users_pkey"), arrayOf(Users.USERS.USER_ID), true)

// -------------------------------------------------------------------------
// FOREIGN KEY definitions
// -------------------------------------------------------------------------

val AMENITY__AMENITY_HOTEL_ID_FKEY: ForeignKey<AmenityRecord, HotelRecord> = Internal.createForeignKey(Amenity.AMENITY, DSL.name("amenity_hotel_id_fkey"), arrayOf(Amenity.AMENITY.HOTEL_ID), com.trip_jr.tripJr.jooq.keys.HOTEL_PKEY, arrayOf(Hotel.HOTEL.HOTEL_ID), true)
val BOOKING__BOOKING_HOTEL_ID_FKEY: ForeignKey<BookingRecord, HotelRecord> = Internal.createForeignKey(Booking.BOOKING, DSL.name("booking_hotel_id_fkey"), arrayOf(Booking.BOOKING.HOTEL_ID), com.trip_jr.tripJr.jooq.keys.HOTEL_PKEY, arrayOf(Hotel.HOTEL.HOTEL_ID), true)
val BOOKING__BOOKING_USER_ID_FKEY: ForeignKey<BookingRecord, UsersRecord> = Internal.createForeignKey(Booking.BOOKING, DSL.name("booking_user_id_fkey"), arrayOf(Booking.BOOKING.USER_ID), com.trip_jr.tripJr.jooq.keys.USERS_PKEY, arrayOf(Users.USERS.USER_ID), true)
val BOOKING__FK_BOOKING_ROOM_ID: ForeignKey<BookingRecord, RoomRecord> = Internal.createForeignKey(Booking.BOOKING, DSL.name("fk_booking_room_id"), arrayOf(Booking.BOOKING.ROOM_ID), com.trip_jr.tripJr.jooq.keys.ROOM_PKEY, arrayOf(Room.ROOM.ROOM_ID), true)
val HOTEL__FK_LOCATION: ForeignKey<HotelRecord, LocationRecord> = Internal.createForeignKey(Hotel.HOTEL, DSL.name("fk_location"), arrayOf(Hotel.HOTEL.LOCATION_ID), com.trip_jr.tripJr.jooq.keys.LOCATION_PKEY, arrayOf(Location.LOCATION.LOCATION_ID), true)
val HOTEL_CLAIM__HOTEL_CLAIMS_HOTEL_ID_FKEY: ForeignKey<HotelClaimRecord, HotelRecord> = Internal.createForeignKey(HotelClaim.HOTEL_CLAIM, DSL.name("hotel_claims_hotel_id_fkey"), arrayOf(HotelClaim.HOTEL_CLAIM.HOTEL_ID), com.trip_jr.tripJr.jooq.keys.HOTEL_PKEY, arrayOf(Hotel.HOTEL.HOTEL_ID), true)
val HOTEL_CLAIM__HOTEL_CLAIMS_USER_ID_FKEY: ForeignKey<HotelClaimRecord, UsersRecord> = Internal.createForeignKey(HotelClaim.HOTEL_CLAIM, DSL.name("hotel_claims_user_id_fkey"), arrayOf(HotelClaim.HOTEL_CLAIM.USER_ID), com.trip_jr.tripJr.jooq.keys.USERS_PKEY, arrayOf(Users.USERS.USER_ID), true)
val REVIEW__REVIEW_HOTEL_ID_FKEY: ForeignKey<ReviewRecord, HotelRecord> = Internal.createForeignKey(Review.REVIEW, DSL.name("review_hotel_id_fkey"), arrayOf(Review.REVIEW.HOTEL_ID), com.trip_jr.tripJr.jooq.keys.HOTEL_PKEY, arrayOf(Hotel.HOTEL.HOTEL_ID), true)
val REVIEW__REVIEW_USER_ID_FKEY: ForeignKey<ReviewRecord, UsersRecord> = Internal.createForeignKey(Review.REVIEW, DSL.name("review_user_id_fkey"), arrayOf(Review.REVIEW.USER_ID), com.trip_jr.tripJr.jooq.keys.USERS_PKEY, arrayOf(Users.USERS.USER_ID), true)
val ROOM__FK_HOTEL_ID: ForeignKey<RoomRecord, HotelRecord> = Internal.createForeignKey(Room.ROOM, DSL.name("fk_hotel_id"), arrayOf(Room.ROOM.HOTEL_ID), com.trip_jr.tripJr.jooq.keys.HOTEL_PKEY, arrayOf(Hotel.HOTEL.HOTEL_ID), true)
val ROOM__FK_RATE_ID: ForeignKey<RoomRecord, RateRecord> = Internal.createForeignKey(Room.ROOM, DSL.name("fk_rate_id"), arrayOf(Room.ROOM.RATE_ID), com.trip_jr.tripJr.jooq.keys.RATE_PKEY, arrayOf(Rate.RATE.RATE_ID), true)
val ROOM__ROOM_HOTEL_ID_FKEY: ForeignKey<RoomRecord, HotelRecord> = Internal.createForeignKey(Room.ROOM, DSL.name("room_hotel_id_fkey"), arrayOf(Room.ROOM.HOTEL_ID), com.trip_jr.tripJr.jooq.keys.HOTEL_PKEY, arrayOf(Hotel.HOTEL.HOTEL_ID), true)
