/*
 * This file is generated by jOOQ.
 */
package com.trip_jr.tripJr.jooq


import com.trip_jr.tripJr.jooq.tables.Hotel
import com.trip_jr.tripJr.jooq.tables.Location

import kotlin.collections.List

import org.jooq.Catalog
import org.jooq.Table
import org.jooq.impl.SchemaImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class Public : SchemaImpl("public", DefaultCatalog.DEFAULT_CATALOG) {
    public companion object {

        /**
         * The reference instance of <code>public</code>
         */
        val PUBLIC: Public = Public()
    }

    /**
     * The table <code>public.hotel</code>.
     */
    val HOTEL: Hotel get() = Hotel.HOTEL

    /**
     * The table <code>public.location</code>.
     */
    val LOCATION: Location get() = Location.LOCATION

    override fun getCatalog(): Catalog = DefaultCatalog.DEFAULT_CATALOG

    override fun getTables(): List<Table<*>> = listOf(
        Hotel.HOTEL,
        Location.LOCATION
    )
}
