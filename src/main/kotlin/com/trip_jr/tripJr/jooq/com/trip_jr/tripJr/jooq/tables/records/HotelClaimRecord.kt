/*
 * This file is generated by jOOQ.
 */
package com.trip_jr.tripJr.jooq.tables.records


import com.trip_jr.tripJr.jooq.enums.ClaimStatus
import com.trip_jr.tripJr.jooq.tables.HotelClaim

import java.time.OffsetDateTime
import java.util.UUID

import org.jooq.Record1
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class HotelClaimRecord() : UpdatableRecordImpl<HotelClaimRecord>(HotelClaim.HOTEL_CLAIM) {

    open var claimId: UUID?
        set(value): Unit = set(0, value)
        get(): UUID? = get(0) as UUID?

    open var userId: UUID?
        set(value): Unit = set(1, value)
        get(): UUID? = get(1) as UUID?

    open var hotelId: UUID?
        set(value): Unit = set(2, value)
        get(): UUID? = get(2) as UUID?

    open var status: ClaimStatus?
        set(value): Unit = set(3, value)
        get(): ClaimStatus? = get(3) as ClaimStatus?

    open var createdAt: OffsetDateTime?
        set(value): Unit = set(4, value)
        get(): OffsetDateTime? = get(4) as OffsetDateTime?

    open var updatedAt: OffsetDateTime?
        set(value): Unit = set(5, value)
        get(): OffsetDateTime? = get(5) as OffsetDateTime?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<UUID?> = super.key() as Record1<UUID?>

    /**
     * Create a detached, initialised HotelClaimRecord
     */
    constructor(claimId: UUID? = null, userId: UUID? = null, hotelId: UUID? = null, status: ClaimStatus? = null, createdAt: OffsetDateTime? = null, updatedAt: OffsetDateTime? = null): this() {
        this.claimId = claimId
        this.userId = userId
        this.hotelId = hotelId
        this.status = status
        this.createdAt = createdAt
        this.updatedAt = updatedAt
        resetChangedOnNotNull()
    }
}
