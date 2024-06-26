/*
 * This file is generated by jOOQ.
 */
package com.trip_jr.tripJr.jooq.enums


import com.trip_jr.tripJr.jooq.Public

import org.jooq.Catalog
import org.jooq.EnumType
import org.jooq.Schema


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
enum class BedType(@get:JvmName("literal") public val literal: String) : EnumType {
    Single("Single"),
    Double("Double"),
    King("King"),
    Queen("Queen");
    override fun getCatalog(): Catalog? = schema.catalog
    override fun getSchema(): Schema = Public.PUBLIC
    override fun getName(): String = "bed_type"
    override fun getLiteral(): String = literal
}
