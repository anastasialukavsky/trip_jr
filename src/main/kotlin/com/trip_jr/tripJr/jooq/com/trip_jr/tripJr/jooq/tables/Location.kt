/*
 * This file is generated by jOOQ.
 */
package com.trip_jr.tripJr.jooq.tables


import com.trip_jr.tripJr.jooq.Public
import com.trip_jr.tripJr.jooq.keys.HOTEL__FK_LOCATION
import com.trip_jr.tripJr.jooq.keys.LOCATION_PKEY
import com.trip_jr.tripJr.jooq.tables.Hotel.HotelPath
import com.trip_jr.tripJr.jooq.tables.records.LocationRecord

import java.util.UUID

import kotlin.collections.Collection

import org.jooq.Condition
import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.InverseForeignKey
import org.jooq.Name
import org.jooq.Path
import org.jooq.PlainSQL
import org.jooq.QueryPart
import org.jooq.Record
import org.jooq.SQL
import org.jooq.Schema
import org.jooq.Select
import org.jooq.Stringly
import org.jooq.Table
import org.jooq.TableField
import org.jooq.TableOptions
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType
import org.jooq.impl.TableImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class Location(
    alias: Name,
    path: Table<out Record>?,
    childPath: ForeignKey<out Record, LocationRecord>?,
    parentPath: InverseForeignKey<out Record, LocationRecord>?,
    aliased: Table<LocationRecord>?,
    parameters: Array<Field<*>?>?,
    where: Condition?
): TableImpl<LocationRecord>(
    alias,
    Public.PUBLIC,
    path,
    childPath,
    parentPath,
    aliased,
    parameters,
    DSL.comment(""),
    TableOptions.table(),
    where,
) {
    companion object {

        /**
         * The reference instance of <code>public.location</code>
         */
        val LOCATION: Location = Location()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<LocationRecord> = LocationRecord::class.java

    /**
     * The column <code>public.location.location_id</code>.
     */
    val LOCATION_ID: TableField<LocationRecord, UUID?> = createField(DSL.name("location_id"), SQLDataType.UUID.nullable(false).defaultValue(DSL.field(DSL.raw("uuid_generate_v4()"), SQLDataType.UUID)), this, "")

    /**
     * The column <code>public.location.phone_number</code>.
     */
    val PHONE_NUMBER: TableField<LocationRecord, String?> = createField(DSL.name("phone_number"), SQLDataType.VARCHAR(20), this, "")

    /**
     * The column <code>public.location.address</code>.
     */
    val ADDRESS: TableField<LocationRecord, String?> = createField(DSL.name("address"), SQLDataType.VARCHAR(255), this, "")

    /**
     * The column <code>public.location.city</code>.
     */
    val CITY: TableField<LocationRecord, String?> = createField(DSL.name("city"), SQLDataType.VARCHAR(100), this, "")

    /**
     * The column <code>public.location.state</code>.
     */
    val STATE: TableField<LocationRecord, String?> = createField(DSL.name("state"), SQLDataType.VARCHAR(100), this, "")

    /**
     * The column <code>public.location.zip</code>.
     */
    val ZIP: TableField<LocationRecord, String?> = createField(DSL.name("zip"), SQLDataType.VARCHAR(20), this, "")

    /**
     * The column <code>public.location.latitude</code>.
     */
    val LATITUDE: TableField<LocationRecord, Double?> = createField(DSL.name("latitude"), SQLDataType.DOUBLE, this, "")

    /**
     * The column <code>public.location.longitude</code>.
     */
    val LONGITUDE: TableField<LocationRecord, Double?> = createField(DSL.name("longitude"), SQLDataType.DOUBLE, this, "")

    private constructor(alias: Name, aliased: Table<LocationRecord>?): this(alias, null, null, null, aliased, null, null)
    private constructor(alias: Name, aliased: Table<LocationRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, null, aliased, parameters, null)
    private constructor(alias: Name, aliased: Table<LocationRecord>?, where: Condition?): this(alias, null, null, null, aliased, null, where)

    /**
     * Create an aliased <code>public.location</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>public.location</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>public.location</code> table reference
     */
    constructor(): this(DSL.name("location"), null)

    constructor(path: Table<out Record>, childPath: ForeignKey<out Record, LocationRecord>?, parentPath: InverseForeignKey<out Record, LocationRecord>?): this(Internal.createPathAlias(path, childPath, parentPath), path, childPath, parentPath, LOCATION, null, null)

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    open class LocationPath : Location, Path<LocationRecord> {
        constructor(path: Table<out Record>, childPath: ForeignKey<out Record, LocationRecord>?, parentPath: InverseForeignKey<out Record, LocationRecord>?): super(path, childPath, parentPath)
        private constructor(alias: Name, aliased: Table<LocationRecord>): super(alias, aliased)
        override fun `as`(alias: String): LocationPath = LocationPath(DSL.name(alias), this)
        override fun `as`(alias: Name): LocationPath = LocationPath(alias, this)
        override fun `as`(alias: Table<*>): LocationPath = LocationPath(alias.qualifiedName, this)
    }
    override fun getSchema(): Schema? = if (aliased()) null else Public.PUBLIC
    override fun getPrimaryKey(): UniqueKey<LocationRecord> = LOCATION_PKEY

    private lateinit var _hotel: HotelPath

    /**
     * Get the implicit to-many join path to the <code>public.hotel</code> table
     */
    fun hotel(): HotelPath {
        if (!this::_hotel.isInitialized)
            _hotel = HotelPath(this, null, HOTEL__FK_LOCATION.inverseKey)

        return _hotel;
    }

    val hotel: HotelPath
        get(): HotelPath = hotel()
    override fun `as`(alias: String): Location = Location(DSL.name(alias), this)
    override fun `as`(alias: Name): Location = Location(alias, this)
    override fun `as`(alias: Table<*>): Location = Location(alias.qualifiedName, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): Location = Location(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): Location = Location(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): Location = Location(name.qualifiedName, null)

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Condition?): Location = Location(qualifiedName, if (aliased()) this else null, condition)

    /**
     * Create an inline derived table from this table
     */
    override fun where(conditions: Collection<Condition>): Location = where(DSL.and(conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(vararg conditions: Condition?): Location = where(DSL.and(*conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Field<Boolean?>?): Location = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(condition: SQL): Location = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String): Location = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg binds: Any?): Location = where(DSL.condition(condition, *binds))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg parts: QueryPart): Location = where(DSL.condition(condition, *parts))

    /**
     * Create an inline derived table from this table
     */
    override fun whereExists(select: Select<*>): Location = where(DSL.exists(select))

    /**
     * Create an inline derived table from this table
     */
    override fun whereNotExists(select: Select<*>): Location = where(DSL.notExists(select))
}
