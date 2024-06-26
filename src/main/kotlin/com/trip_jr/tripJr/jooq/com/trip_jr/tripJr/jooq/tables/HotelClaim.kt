/*
 * This file is generated by jOOQ.
 */
package com.trip_jr.tripJr.jooq.tables


import com.trip_jr.tripJr.jooq.Public
import com.trip_jr.tripJr.jooq.enums.ClaimStatus
import com.trip_jr.tripJr.jooq.keys.HOTEL_CLAIMS_PKEY
import com.trip_jr.tripJr.jooq.keys.HOTEL_CLAIM__HOTEL_CLAIMS_HOTEL_ID_FKEY
import com.trip_jr.tripJr.jooq.keys.HOTEL_CLAIM__HOTEL_CLAIMS_USER_ID_FKEY
import com.trip_jr.tripJr.jooq.tables.Hotel.HotelPath
import com.trip_jr.tripJr.jooq.tables.Users.UsersPath
import com.trip_jr.tripJr.jooq.tables.records.HotelClaimRecord

import java.time.OffsetDateTime
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
open class HotelClaim(
    alias: Name,
    path: Table<out Record>?,
    childPath: ForeignKey<out Record, HotelClaimRecord>?,
    parentPath: InverseForeignKey<out Record, HotelClaimRecord>?,
    aliased: Table<HotelClaimRecord>?,
    parameters: Array<Field<*>?>?,
    where: Condition?
): TableImpl<HotelClaimRecord>(
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
         * The reference instance of <code>public.hotel_claim</code>
         */
        val HOTEL_CLAIM: HotelClaim = HotelClaim()
    }

    /**
     * The class holding records for this type
     */
    override fun getRecordType(): Class<HotelClaimRecord> = HotelClaimRecord::class.java

    /**
     * The column <code>public.hotel_claim.claim_id</code>.
     */
    val CLAIM_ID: TableField<HotelClaimRecord, UUID?> = createField(DSL.name("claim_id"), SQLDataType.UUID.nullable(false), this, "")

    /**
     * The column <code>public.hotel_claim.user_id</code>.
     */
    val USER_ID: TableField<HotelClaimRecord, UUID?> = createField(DSL.name("user_id"), SQLDataType.UUID, this, "")

    /**
     * The column <code>public.hotel_claim.hotel_id</code>.
     */
    val HOTEL_ID: TableField<HotelClaimRecord, UUID?> = createField(DSL.name("hotel_id"), SQLDataType.UUID, this, "")

    /**
     * The column <code>public.hotel_claim.status</code>.
     */
    val STATUS: TableField<HotelClaimRecord, ClaimStatus?> = createField(DSL.name("status"), SQLDataType.VARCHAR.defaultValue(DSL.field(DSL.raw("'UNCLAIMED'::claim_status"), SQLDataType.VARCHAR)).asEnumDataType(ClaimStatus::class.java), this, "")

    /**
     * The column <code>public.hotel_claim.created_at</code>.
     */
    val CREATED_AT: TableField<HotelClaimRecord, OffsetDateTime?> = createField(DSL.name("created_at"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).defaultValue(DSL.field(DSL.raw("CURRENT_TIMESTAMP"), SQLDataType.TIMESTAMPWITHTIMEZONE)), this, "")

    /**
     * The column <code>public.hotel_claim.updated_at</code>.
     */
    val UPDATED_AT: TableField<HotelClaimRecord, OffsetDateTime?> = createField(DSL.name("updated_at"), SQLDataType.TIMESTAMPWITHTIMEZONE(6).defaultValue(DSL.field(DSL.raw("CURRENT_TIMESTAMP"), SQLDataType.TIMESTAMPWITHTIMEZONE)), this, "")

    private constructor(alias: Name, aliased: Table<HotelClaimRecord>?): this(alias, null, null, null, aliased, null, null)
    private constructor(alias: Name, aliased: Table<HotelClaimRecord>?, parameters: Array<Field<*>?>?): this(alias, null, null, null, aliased, parameters, null)
    private constructor(alias: Name, aliased: Table<HotelClaimRecord>?, where: Condition?): this(alias, null, null, null, aliased, null, where)

    /**
     * Create an aliased <code>public.hotel_claim</code> table reference
     */
    constructor(alias: String): this(DSL.name(alias))

    /**
     * Create an aliased <code>public.hotel_claim</code> table reference
     */
    constructor(alias: Name): this(alias, null)

    /**
     * Create a <code>public.hotel_claim</code> table reference
     */
    constructor(): this(DSL.name("hotel_claim"), null)

    constructor(path: Table<out Record>, childPath: ForeignKey<out Record, HotelClaimRecord>?, parentPath: InverseForeignKey<out Record, HotelClaimRecord>?): this(Internal.createPathAlias(path, childPath, parentPath), path, childPath, parentPath, HOTEL_CLAIM, null, null)

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    open class HotelClaimPath : HotelClaim, Path<HotelClaimRecord> {
        constructor(path: Table<out Record>, childPath: ForeignKey<out Record, HotelClaimRecord>?, parentPath: InverseForeignKey<out Record, HotelClaimRecord>?): super(path, childPath, parentPath)
        private constructor(alias: Name, aliased: Table<HotelClaimRecord>): super(alias, aliased)
        override fun `as`(alias: String): HotelClaimPath = HotelClaimPath(DSL.name(alias), this)
        override fun `as`(alias: Name): HotelClaimPath = HotelClaimPath(alias, this)
        override fun `as`(alias: Table<*>): HotelClaimPath = HotelClaimPath(alias.qualifiedName, this)
    }
    override fun getSchema(): Schema? = if (aliased()) null else Public.PUBLIC
    override fun getPrimaryKey(): UniqueKey<HotelClaimRecord> = HOTEL_CLAIMS_PKEY
    override fun getReferences(): List<ForeignKey<HotelClaimRecord, *>> = listOf(HOTEL_CLAIM__HOTEL_CLAIMS_USER_ID_FKEY, HOTEL_CLAIM__HOTEL_CLAIMS_HOTEL_ID_FKEY)

    private lateinit var _users: UsersPath

    /**
     * Get the implicit join path to the <code>public.users</code> table.
     */
    fun users(): UsersPath {
        if (!this::_users.isInitialized)
            _users = UsersPath(this, HOTEL_CLAIM__HOTEL_CLAIMS_USER_ID_FKEY, null)

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
            _hotel = HotelPath(this, HOTEL_CLAIM__HOTEL_CLAIMS_HOTEL_ID_FKEY, null)

        return _hotel;
    }

    val hotel: HotelPath
        get(): HotelPath = hotel()
    override fun `as`(alias: String): HotelClaim = HotelClaim(DSL.name(alias), this)
    override fun `as`(alias: Name): HotelClaim = HotelClaim(alias, this)
    override fun `as`(alias: Table<*>): HotelClaim = HotelClaim(alias.qualifiedName, this)

    /**
     * Rename this table
     */
    override fun rename(name: String): HotelClaim = HotelClaim(DSL.name(name), null)

    /**
     * Rename this table
     */
    override fun rename(name: Name): HotelClaim = HotelClaim(name, null)

    /**
     * Rename this table
     */
    override fun rename(name: Table<*>): HotelClaim = HotelClaim(name.qualifiedName, null)

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Condition?): HotelClaim = HotelClaim(qualifiedName, if (aliased()) this else null, condition)

    /**
     * Create an inline derived table from this table
     */
    override fun where(conditions: Collection<Condition>): HotelClaim = where(DSL.and(conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(vararg conditions: Condition?): HotelClaim = where(DSL.and(*conditions))

    /**
     * Create an inline derived table from this table
     */
    override fun where(condition: Field<Boolean?>?): HotelClaim = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(condition: SQL): HotelClaim = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String): HotelClaim = where(DSL.condition(condition))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg binds: Any?): HotelClaim = where(DSL.condition(condition, *binds))

    /**
     * Create an inline derived table from this table
     */
    @PlainSQL override fun where(@Stringly.SQL condition: String, vararg parts: QueryPart): HotelClaim = where(DSL.condition(condition, *parts))

    /**
     * Create an inline derived table from this table
     */
    override fun whereExists(select: Select<*>): HotelClaim = where(DSL.exists(select))

    /**
     * Create an inline derived table from this table
     */
    override fun whereNotExists(select: Select<*>): HotelClaim = where(DSL.notExists(select))
}
