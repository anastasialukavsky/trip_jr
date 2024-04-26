/*
 * This file is generated by jOOQ.
 */
package com.trip_jr.tripJr.jooq.tables


import com.trip_jr.tripJr.jooq.Public
import com.trip_jr.tripJr.jooq.keys.BOOKING_PKEY
import com.trip_jr.tripJr.jooq.keys.BOOKING__BOOKING_HOTEL_ID_FKEY
import com.trip_jr.tripJr.jooq.keys.BOOKING__BOOKING_USER_ID_FKEY
import com.trip_jr.tripJr.jooq.tables.Hotel.HotelPath
import com.trip_jr.tripJr.jooq.tables.Users.UsersPath
import com.trip_jr.tripJr.jooq.tables.records.BookingRecord

import java.math.BigDecimal
import java.time.LocalDate
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
open class Booking(
    alias: Name,
    path: Table<out Record>?,
    childPath: ForeignKey<out Record, BookingRecord>?,
    parentPath: InverseForeignKey<out Record, BookingRecord>?,
    aliased: Table<BookingRecord>?,
    parameters: Array<Field<*>?>?,
    where: Condition?
): TableImpl<BookingRecord>(
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
         * The reference instance of <code>public.booking</code>
         */
        val BOOKING: Booking = Booking()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<BookingRecord> = BookingRecord::class.java

    /**
     * The column <code>public.booking.booking_id</code>.
     */
    val BOOKING_ID: TableField<BookingRecord, UUID?> = createField(DSL.name("booking_id"), SQLDataType.UUID.nullable(false), this, "")

    /**
     * The column <code>public.booking.user_id</code>.
     */
    val USER_ID: TableField<BookingRecord, UUID?> = createField(DSL.name("user_id"), SQLDataType.UUID.nullable(false), this, "")

    /**
     * The column <code>public.booking.hotel_id</code>.
     */
    val HOTEL_ID: TableField<BookingRecord, UUID?> = createField(DSL.name("hotel_id"), SQLDataType.UUID.nullable(false), this, "")

    /**
     * The column <code>public.booking.check_in_date</code>.
     */
    val CHECK_IN_DATE: TableField<BookingRecord, LocalDate?> = createField(DSL.name("check_in_date"), SQLDataType.LOCALDATE.nullable(false), this, "")

    /**
     * The column <code>public.booking.check_out_date</code>.
     */
    val CHECK_OUT_DATE: TableField<BookingRecord, LocalDate?> = createField(DSL.name("check_out_date"), SQLDataType.LOCALDATE.nullable(false), this, "")

    /**
     * The column <code>public.booking.total_cost</code>.
     */
    val TOTAL_COST: TableField<BookingRecord, BigDecimal?> = createField(DSL.name("total_cost"), SQLDataType.NUMERIC(10, 2).nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<BookingRecord>?): this(alias, null, null, null, aliased, null, null)
    private constructor(alias: Name, aliased: Table<BookingRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, null, aliased, parameters, null)
    private constructor(alias: Name, aliased: Table<BookingRecord>?, where: Condition?): this(alias, null, null, null, aliased, null, where)

    /**
     * Create an aliased <code>public.booking</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>public.booking</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>public.booking</code> table reference
     */
    constructor(): this(DSL.name("booking"), null)

    constructor(path: Table<out Record>, childPath: ForeignKey<out Record, BookingRecord>?, parentPath: InverseForeignKey<out Record, BookingRecord>?): this(Internal.createPathAlias(path, childPath, parentPath), path, childPath, parentPath, BOOKING, null, null)

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    open class BookingPath : Booking, Path<BookingRecord> {
        constructor(path: Table<out Record>, childPath: ForeignKey<out Record, BookingRecord>?, parentPath: InverseForeignKey<out Record, BookingRecord>?): super(path, childPath, parentPath)
        private constructor(alias: Name, aliased: Table<BookingRecord>): super(alias, aliased)
        override fun `as`(alias: String): BookingPath = BookingPath(DSL.name(alias), this)
        override fun `as`(alias: Name): BookingPath = BookingPath(alias, this)
        override fun `as`(alias: Table<*>): BookingPath = BookingPath(alias.qualifiedName, this)
    }
    override fun getSchema(): Schema? = if (aliased()) null else Public.PUBLIC
    override fun getPrimaryKey(): UniqueKey<BookingRecord> = BOOKING_PKEY
    override fun getReferences(): List<ForeignKey<BookingRecord, *>> = listOf(BOOKING__BOOKING_USER_ID_FKEY, BOOKING__BOOKING_HOTEL_ID_FKEY)

    private lateinit var _users: UsersPath

    /**
     * Get the implicit join path to the <code>public.users</code> table.
     */
    fun users(): UsersPath {
        if (!this::_users.isInitialized)
            _users = UsersPath(this, BOOKING__BOOKING_USER_ID_FKEY, null)

        return _users;
    }

    val users: UsersPath
        get(): UsersPath = users()

    private lateinit var _hotel: HotelPath

    /**
     * Get the implicit join path to the <code>public.hotel</code> table.
     */
    fun hotel(): HotelPath {
        if (!this::_hotel.isInitialized)
            _hotel = HotelPath(this, BOOKING__BOOKING_HOTEL_ID_FKEY, null)

        return _hotel;
    }

    val hotel: HotelPath
        get(): HotelPath = hotel()
    override fun `as`(alias: String): Booking = Booking(DSL.name(alias), this)
    override fun `as`(alias: Name): Booking = Booking(alias, this)
    override fun `as`(alias: Table<*>): Booking = Booking(alias.qualifiedName, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): Booking = Booking(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): Booking = Booking(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): Booking = Booking(name.qualifiedName, null)

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Condition?): Booking = Booking(qualifiedName, if (aliased()) this else null, condition)

    /**
     * Create an inline derived table from this table
     */
    override fun where(conditions: Collection<Condition>): Booking = where(DSL.and(conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(vararg conditions: Condition?): Booking = where(DSL.and(*conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Field<Boolean?>?): Booking = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(condition: SQL): Booking = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String): Booking = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg binds: Any?): Booking = where(DSL.condition(condition, *binds))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg parts: QueryPart): Booking = where(DSL.condition(condition, *parts))

    /**
     * Create an inline derived table from this table
     */
    override fun whereExists(select: Select<*>): Booking = where(DSL.exists(select))

    /**
     * Create an inline derived table from this table
     */
    override fun whereNotExists(select: Select<*>): Booking = where(DSL.notExists(select))
}