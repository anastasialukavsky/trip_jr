/*
 * This file is generated by jOOQ.
 */
package com.trip_jr.tripJr.jooq.tables.records


import com.trip_jr.tripJr.jooq.tables.Location

import java.time.OffsetDateTime
import java.util.UUID

import org.jooq.Record1
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class LocationRecord() : UpdatableRecordImpl<LocationRecord>(Location.LOCATION) {

    open var locationId: UUID?
        set(value): Unit = set(0, value)
        get(): UUID? = get(0) as UUID?

    open var phoneNumber: String?
        set(value): Unit = set(1, value)
        get(): String? = get(1) as String?

    open var address: String?
        set(value): Unit = set(2, value)
        get(): String? = get(2) as String?

    open var city: String?
        set(value): Unit = set(3, value)
        get(): String? = get(3) as String?

    open var state: String?
        set(value): Unit = set(4, value)
        get(): String? = get(4) as String?

    open var zip: String?
        set(value): Unit = set(5, value)
        get(): String? = get(5) as String?

    open var latitude: Double?
        set(value): Unit = set(6, value)
        get(): Double? = get(6) as Double?

    open var longitude: Double?
        set(value): Unit = set(7, value)
        get(): Double? = get(7) as Double?

    open var createdAt: OffsetDateTime?
        set(value): Unit = set(8, value)
        get(): OffsetDateTime? = get(8) as OffsetDateTime?

    open var updatedAt: OffsetDateTime?
        set(value): Unit = set(9, value)
        get(): OffsetDateTime? = get(9) as OffsetDateTime?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<UUID?> = super.key() as Record1<UUID?>

    /**
     * Create a detached, initialised LocationRecord
     */
    constructor(locationId: UUID? = null, phoneNumber: String? = null, address: String? = null, city: String? = null, state: String? = null, zip: String? = null, latitude: Double? = null, longitude: Double? = null, createdAt: OffsetDateTime? = null, updatedAt: OffsetDateTime? = null): this() {
        this.locationId = locationId
        this.phoneNumber = phoneNumber
        this.address = address
        this.city = city
        this.state = state
        this.zip = zip
        this.latitude = latitude
        this.longitude = longitude
        this.createdAt = createdAt
        this.updatedAt = updatedAt
        resetChangedOnNotNull()
    }
}
