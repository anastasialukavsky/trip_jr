/*
 * This file is generated by jOOQ.
 */
package com.trip_jr.tripJr.jooq.tables


import com.trip_jr.tripJr.jooq.Public
import com.trip_jr.tripJr.jooq.enums.BedType
import com.trip_jr.tripJr.jooq.enums.RoomStatus
import com.trip_jr.tripJr.jooq.enums.RoomType
import com.trip_jr.tripJr.jooq.keys.BOOKING__FK_BOOKING_ROOM_ID
import com.trip_jr.tripJr.jooq.keys.ROOM_PKEY
import com.trip_jr.tripJr.jooq.keys.ROOM__FK_HOTEL_ID
import com.trip_jr.tripJr.jooq.keys.ROOM__FK_RATE_ID
import com.trip_jr.tripJr.jooq.keys.ROOM__ROOM_HOTEL_ID_FKEY
import com.trip_jr.tripJr.jooq.tables.Booking.BookingPath
import com.trip_jr.tripJr.jooq.tables.Hotel.HotelPath
import com.trip_jr.tripJr.jooq.tables.Rate.RatePath
import com.trip_jr.tripJr.jooq.tables.records.RoomRecord

import java.time.OffsetDateTime
import java.util.UUID

import kotlin.collections.Collection
import kotlin.collections.List

import org.jooq.Condition
import org.jooq.Field
import org.jooq.ForeignKey
import org.jooq.Identity
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
open class Room(
    alias: Name,
    path: Table<out Record>?,
    childPath: ForeignKey<out Record, RoomRecord>?,
    parentPath: InverseForeignKey<out Record, RoomRecord>?,
    aliased: Table<RoomRecord>?,
    parameters: Array<Field<*>?>?,
    where: Condition?
): TableImpl<RoomRecord>(
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
         * The reference instance of <code>public.room</code>
         */
        val ROOM: Room = Room()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<RoomRecord> = RoomRecord::class.java

    /**
     * The column <code>public.room.room_number</code>.
     */
    val ROOM_NUMBER: TableField<RoomRecord, Int?> = createField(DSL.name("room_number"), SQLDataType.INTEGER.nullable(false).identity(true), this, "")

    /**
     * The column <code>public.room.room_type</code>.
     */
    val ROOM_TYPE: TableField<RoomRecord, RoomType?> = createField(DSL.name("room_type"), SQLDataType.VARCHAR.asEnumDataType(RoomType::class.java), this, "")

    /**
     * The column <code>public.room.room_status</code>.
     */
    val ROOM_STATUS: TableField<RoomRecord, RoomStatus?> = createField(DSL.name("room_status"), SQLDataType.VARCHAR.asEnumDataType(RoomStatus::class.java), this, "")

    /**
     * The column <code>public.room.bed_type</code>.
     */
    val BED_TYPE: TableField<RoomRecord, BedType?> = createField(DSL.name("bed_type"), SQLDataType.VARCHAR.asEnumDataType(BedType::class.java), this, "")

    /**
     * The column <code>public.room.maximum_occupancy</code>.
     */
    val MAXIMUM_OCCUPANCY: TableField<RoomRecord, Int?> = createField(DSL.name("maximum_occupancy"), SQLDataType.INTEGER, this, "")

    /**
     * The column <code>public.room.description</code>.
     */
    val DESCRIPTION: TableField<RoomRecord, String?> = createField(DSL.name("description"), SQLDataType.CLOB, this, "")

    /**
     * The column <code>public.room.floor</code>.
     */
    val FLOOR: TableField<RoomRecord, Int?> = createField(DSL.name("floor"), SQLDataType.INTEGER, this, "")

    /**
     * The column <code>public.room.availability</code>.
     */
    val AVAILABILITY: TableField<RoomRecord, Boolean?> = createField(DSL.name("availability"), SQLDataType.BOOLEAN, this, "")

    /**
     * The column <code>public.room.last_cleaned</code>.
     */
    val LAST_CLEANED: TableField<RoomRecord, OffsetDateTime?> = createField(DSL.name("last_cleaned"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).defaultValue(DSL.field(DSL.raw("CURRENT_TIMESTAMP"), SQLDataType.TIMESTAMPWITHTIMEZONE)), this, "")

    /**
     * The column <code>public.room.created_at</code>.
     */
    val CREATED_AT: TableField<RoomRecord, OffsetDateTime?> = createField(DSL.name("created_at"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).defaultValue(DSL.field(DSL.raw("CURRENT_TIMESTAMP"), SQLDataType.TIMESTAMPWITHTIMEZONE)), this, "")

    /**
     * The column <code>public.room.updated_at</code>.
     */
    val UPDATED_AT: TableField<RoomRecord, OffsetDateTime?> = createField(DSL.name("updated_at"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).defaultValue(DSL.field(DSL.raw("CURRENT_TIMESTAMP"), SQLDataType.TIMESTAMPWITHTIMEZONE)), this, "")

    /**
     * The column <code>public.room.hotel_id</code>.
     */
    val HOTEL_ID: TableField<RoomRecord, UUID?> = createField(DSL.name("hotel_id"), SQLDataType.UUID, this, "")

    /**
     * The column <code>public.room.rate_id</code>.
     */
    val RATE_ID: TableField<RoomRecord, UUID?> = createField(DSL.name("rate_id"), SQLDataType.UUID, this, "")

    /**
     * The column <code>public.room.room_id</code>.
     */
    val ROOM_ID: TableField<RoomRecord, UUID?> = createField(DSL.name("room_id"), SQLDataType.UUID.nullable(false), this, "")

    private constructor(alias: Name, aliased: Table<RoomRecord>?): this(alias, null, null, null, aliased, null, null)
    private constructor(alias: Name, aliased: Table<RoomRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, null, aliased, parameters, null)
    private constructor(alias: Name, aliased: Table<RoomRecord>?, where: Condition?): this(alias, null, null, null, aliased, null, where)

    /**
     * Create an aliased <code>public.room</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>public.room</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>public.room</code> table reference
     */
    constructor(): this(DSL.name("room"), null)

    constructor(path: Table<out Record>, childPath: ForeignKey<out Record, RoomRecord>?, parentPath: InverseForeignKey<out Record, RoomRecord>?): this(Internal.createPathAlias(path, childPath, parentPath), path, childPath, parentPath, ROOM, null, null)

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    open class RoomPath : Room, Path<RoomRecord> {
        constructor(path: Table<out Record>, childPath: ForeignKey<out Record, RoomRecord>?, parentPath: InverseForeignKey<out Record, RoomRecord>?): super(path, childPath, parentPath)
        private constructor(alias: Name, aliased: Table<RoomRecord>): super(alias, aliased)
        override fun `as`(alias: String): RoomPath = RoomPath(DSL.name(alias), this)
        override fun `as`(alias: Name): RoomPath = RoomPath(alias, this)
        override fun `as`(alias: Table<*>): RoomPath = RoomPath(alias.qualifiedName, this)
    }
    override fun getSchema(): Schema? = if (aliased()) null else Public.PUBLIC
    override fun getIdentity(): Identity<RoomRecord, Int?> = super.getIdentity() as Identity<RoomRecord, Int?>
    override fun getPrimaryKey(): UniqueKey<RoomRecord> = ROOM_PKEY
    override fun getReferences(): List<ForeignKey<RoomRecord, *>> = listOf(ROOM__FK_HOTEL_ID, ROOM__ROOM_HOTEL_ID_FKEY, ROOM__FK_RATE_ID)

    private lateinit var _fkHotelId: HotelPath

    /**
     * Get the implicit join path to the <code>public.hotel</code> table, via
     * the <code>fk_hotel_id</code> key.
     */
    fun fkHotelId(): HotelPath {
        if (!this::_fkHotelId.isInitialized)
            _fkHotelId = HotelPath(this, ROOM__FK_HOTEL_ID, null)

        return _fkHotelId;
    }

    val fkHotelId: HotelPath
        get(): HotelPath = fkHotelId()

    private lateinit var _roomHotelIdFkey: HotelPath

    /**
     * Get the implicit join path to the <code>public.hotel</code> table, via
     * the <code>room_hotel_id_fkey</code> key.
     */
    fun roomHotelIdFkey(): HotelPath {
        if (!this::_roomHotelIdFkey.isInitialized)
            _roomHotelIdFkey = HotelPath(this, ROOM__ROOM_HOTEL_ID_FKEY, null)

        return _roomHotelIdFkey;
    }

    val roomHotelIdFkey: HotelPath
        get(): HotelPath = roomHotelIdFkey()

    private lateinit var _rate: RatePath

    /**
     * Get the implicit join path to the <code>public.rate</code> table.
     */
    fun rate(): RatePath {
        if (!this::_rate.isInitialized)
            _rate = RatePath(this, ROOM__FK_RATE_ID, null)

        return _rate;
    }

    val rate: RatePath
        get(): RatePath = rate()

    private lateinit var _booking: BookingPath

    /**
     * Get the implicit to-many join path to the <code>public.booking</code>
     * table
     */
    fun booking(): BookingPath {
        if (!this::_booking.isInitialized)
            _booking = BookingPath(this, null, BOOKING__FK_BOOKING_ROOM_ID.inverseKey)

        return _booking;
    }

    val booking: BookingPath
        get(): BookingPath = booking()
    override fun `as`(alias: String): Room = Room(DSL.name(alias), this)
    override fun `as`(alias: Name): Room = Room(alias, this)
    override fun `as`(alias: Table<*>): Room = Room(alias.qualifiedName, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): Room = Room(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): Room = Room(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): Room = Room(name.qualifiedName, null)

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Condition?): Room = Room(qualifiedName, if (aliased()) this else null, condition)

    /**
     * Create an inline derived table from this table
     */
    override fun where(conditions: Collection<Condition>): Room = where(DSL.and(conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(vararg conditions: Condition?): Room = where(DSL.and(*conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Field<Boolean?>?): Room = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(condition: SQL): Room = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String): Room = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg binds: Any?): Room = where(DSL.condition(condition, *binds))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg parts: QueryPart): Room = where(DSL.condition(condition, *parts))

    /**
     * Create an inline derived table from this table
     */
    override fun whereExists(select: Select<*>): Room = where(DSL.exists(select))

    /**
     * Create an inline derived table from this table
     */
    override fun whereNotExists(select: Select<*>): Room = where(DSL.notExists(select))
}
