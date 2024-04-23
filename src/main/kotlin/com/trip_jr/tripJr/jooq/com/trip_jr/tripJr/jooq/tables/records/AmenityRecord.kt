/*
 * This file is generated by jOOQ.
 */
package com.trip_jr.tripJr.jooq.tables.records


import com.trip_jr.tripJr.jooq.tables.Amenity

import java.util.UUID

import org.jooq.Record1
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class AmenityRecord() : UpdatableRecordImpl<AmenityRecord>(Amenity.AMENITY) {

    open var amenityId: UUID?
        set(value): Unit = set(0, value)
        get(): UUID? = get(0) as UUID?

    open var amenityName: String?
        set(value): Unit = set(1, value)
        get(): String? = get(1) as String?

    open var hotelId: UUID?
        set(value): Unit = set(2, value)
        get(): UUID? = get(2) as UUID?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<UUID?> = super.key() as Record1<UUID?>

    /**
     * Create a detached, initialised AmenityRecord
     */
    constructor(amenityId: UUID? = null, amenityName: String? = null, hotelId: UUID? = null): this() {
        this.amenityId = amenityId
        this.amenityName = amenityName
        this.hotelId = hotelId
        resetChangedOnNotNull()
    }
}
