package com.trip_jr.tripJr.service.booking

import com.trip_jr.tripJr.dto.booking.BookingDTO
import com.trip_jr.tripJr.jooq.tables.records.BookingRecord
import com.trip_jr.tripJr.jooq.tables.references.BOOKING
import com.trip_jr.tripJr.service.utils.UUIDUtils
import com.trip_jr.tripJr.service.utils.UserUtils
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@Service
class BookingService {

    @Autowired
    lateinit var dslContext: DSLContext

    @Autowired
    lateinit var uuidUtils: UUIDUtils


    fun getAllBookingsByUserId(userId: UUID): List<BookingDTO> {
        try {
            val bookingsRecord = dslContext
                .select()
                .from(BOOKING)
                .where(BOOKING.USER_ID.eq(userId))
                .fetch()

            return bookingsRecord.into(BookingDTO::class.java)
        } catch (e: Exception) {
            throw e
        }
    }

    fun getSingleBookingByUserId(userId: UUID, bookingId: UUID): BookingDTO? {
        try {
            val bookingRecord = dslContext
                .select()
                .from(BOOKING)
                .where(BOOKING.USER_ID.eq(userId))
                .and(BOOKING.BOOKING_ID.eq(bookingId))
                .fetchOne()

            return bookingRecord?.into(BookingDTO::class.java)
        }catch(e: Exception) {
            throw e
        }
    }

    fun createBooking(booking: BookingDTO): BookingDTO {
        try {
            val bookingId = booking.bookingId ?: uuidUtils.generateUUID()
            val bookingRecord = dslContext.insertInto(BOOKING)
                .set(BOOKING.BOOKING_ID,bookingId)
                .set(BOOKING.USER_ID, booking.userId)
                .set(BOOKING.HOTEL_ID, booking.hotelId)
                .set(BOOKING.CHECK_IN_DATE, booking.checkInDate)
                .set(BOOKING.CHECK_OUT_DATE, booking.checkOutDate)
                .set(BOOKING.TOTAL_COST, booking.totalCost)
                .returningResult(BOOKING.BOOKING_ID)
                .fetchOne()

            bookingRecord ?: throw NullPointerException("Failed to create booking record!")



            return booking.copy(bookingId = bookingRecord.get(BOOKING.BOOKING_ID))


        } catch (e: Exception) {
            throw e
        }
    }
}