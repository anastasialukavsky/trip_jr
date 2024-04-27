/*
 * This file is generated by jOOQ.
 */
package com.trip_jr.tripJr.jooq.tables.records


import com.trip_jr.tripJr.jooq.tables.Review

import java.util.UUID

import org.jooq.Record1
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class ReviewRecord() : UpdatableRecordImpl<ReviewRecord>(Review.REVIEW) {

    open var reviewId: UUID?
        set(value): Unit = set(0, value)
        get(): UUID? = get(0) as UUID?

    open var userId: UUID?
        set(value): Unit = set(1, value)
        get(): UUID? = get(1) as UUID?

    open var hotelId: UUID?
        set(value): Unit = set(2, value)
        get(): UUID? = get(2) as UUID?

    open var rating: Int?
        set(value): Unit = set(3, value)
        get(): Int? = get(3) as Int?

    open var reviewTitle: String?
        set(value): Unit = set(4, value)
        get(): String? = get(4) as String?

    open var reviewBody: String?
        set(value): Unit = set(5, value)
        get(): String? = get(5) as String?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<UUID?> = super.key() as Record1<UUID?>

    /**
     * Create a detached, initialised ReviewRecord
     */
    constructor(reviewId: UUID? = null, userId: UUID? = null, hotelId: UUID? = null, rating: Int? = null, reviewTitle: String? = null, reviewBody: String? = null): this() {
        this.reviewId = reviewId
        this.userId = userId
        this.hotelId = hotelId
        this.rating = rating
        this.reviewTitle = reviewTitle
        this.reviewBody = reviewBody
        resetChangedOnNotNull()
    }
}