/*
 * This file is generated by jOOQ.
 */
package com.trip_jr.tripJr.jooq.tables


import com.trip_jr.tripJr.jooq.Public
import com.trip_jr.tripJr.jooq.keys.AMENITY_PKEY
import com.trip_jr.tripJr.jooq.keys.AMENITY__AMENITY_HOTEL_ID_FKEY
import com.trip_jr.tripJr.jooq.tables.Hotel.HotelPath
import com.trip_jr.tripJr.jooq.tables.records.AmenityRecord

import java.util.UUID

import kotlin.collections.Collection
import kotlin.collections.List

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
open class Amenity(
    alias: Name,
    path: Table<out Record>?,
    childPath: ForeignKey<out Record, AmenityRecord>?,
    parentPath: InverseForeignKey<out Record, AmenityRecord>?,
    aliased: Table<AmenityRecord>?,
    parameters: Array<Field<*>?>?,
    where: Condition?
): TableImpl<AmenityRecord>(
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
         * The reference instance of <code>public.amenity</code>
         */
        val AMENITY: Amenity = Amenity()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<AmenityRecord> = AmenityRecord::class.java

    /**
     * The column <code>public.amenity.amenity_id</code>.
     */
    val AMENITY_ID: TableField<AmenityRecord, UUID?> = createField(DSL.name("amenity_id"), SQLDataType.UUID.nullable(false), this, "")

    /**
     * The column <code>public.amenity.amenity_name</code>.
     */
    val AMENITY_NAME: TableField<AmenityRecord, String?> = createField(DSL.name("amenity_name"), SQLDataType.VARCHAR(255), this, "")

    /**
     * The column <code>public.amenity.hotel_id</code>.
     */
    val HOTEL_ID: TableField<AmenityRecord, UUID?> = createField(DSL.name("hotel_id"), SQLDataType.UUID, this, "")

    private constructor(alias: Name, aliased: Table<AmenityRecord>?): this(alias, null, null, null, aliased, null, null)
    private constructor(alias: Name, aliased: Table<AmenityRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, null, aliased, parameters, null)
    private constructor(alias: Name, aliased: Table<AmenityRecord>?, where: Condition?): this(alias, null, null, null, aliased, null, where)

    /**
     * Create an aliased <code>public.amenity</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>public.amenity</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>public.amenity</code> table reference
     */
    constructor(): this(DSL.name("amenity"), null)

    constructor(path: Table<out Record>, childPath: ForeignKey<out Record, AmenityRecord>?, parentPath: InverseForeignKey<out Record, AmenityRecord>?): this(Internal.createPathAlias(path, childPath, parentPath), path, childPath, parentPath, AMENITY, null, null)

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    open class AmenityPath : Amenity, Path<AmenityRecord> {
        constructor(path: Table<out Record>, childPath: ForeignKey<out Record, AmenityRecord>?, parentPath: InverseForeignKey<out Record, AmenityRecord>?): super(path, childPath, parentPath)
        private constructor(alias: Name, aliased: Table<AmenityRecord>): super(alias, aliased)
        override fun `as`(alias: String): AmenityPath = AmenityPath(DSL.name(alias), this)
        override fun `as`(alias: Name): AmenityPath = AmenityPath(alias, this)
        override fun `as`(alias: Table<*>): AmenityPath = AmenityPath(alias.qualifiedName, this)
    }
    override fun getSchema(): Schema? = if (aliased()) null else Public.PUBLIC
    override fun getPrimaryKey(): UniqueKey<AmenityRecord> = AMENITY_PKEY
    override fun getReferences(): List<ForeignKey<AmenityRecord, *>> = listOf(AMENITY__AMENITY_HOTEL_ID_FKEY)

    private lateinit var _hotel: HotelPath

    /**
     * Get the implicit join path to the <code>public.hotel</code> table.
     */
    fun hotel(): HotelPath {
        if (!this::_hotel.isInitialized)
            _hotel = HotelPath(this, AMENITY__AMENITY_HOTEL_ID_FKEY, null)

        return _hotel;
    }

    val hotel: HotelPath
        get(): HotelPath = hotel()
    override fun `as`(alias: String): Amenity = Amenity(DSL.name(alias), this)
    override fun `as`(alias: Name): Amenity = Amenity(alias, this)
    override fun `as`(alias: Table<*>): Amenity = Amenity(alias.qualifiedName, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): Amenity = Amenity(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): Amenity = Amenity(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): Amenity = Amenity(name.qualifiedName, null)

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Condition?): Amenity = Amenity(qualifiedName, if (aliased()) this else null, condition)

    /**
     * Create an inline derived table from this table
     */
    override fun where(conditions: Collection<Condition>): Amenity = where(DSL.and(conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(vararg conditions: Condition?): Amenity = where(DSL.and(*conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Field<Boolean?>?): Amenity = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(condition: SQL): Amenity = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String): Amenity = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg binds: Any?): Amenity = where(DSL.condition(condition, *binds))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg parts: QueryPart): Amenity = where(DSL.condition(condition, *parts))

    /**
     * Create an inline derived table from this table
     */
    override fun whereExists(select: Select<*>): Amenity = where(DSL.exists(select))

    /**
     * Create an inline derived table from this table
     */
    override fun whereNotExists(select: Select<*>): Amenity = where(DSL.notExists(select))
}
