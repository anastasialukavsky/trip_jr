/*
 * This file is generated by jOOQ.
 */
package com.trip_jr.tripJr.jooq.routines


import com.trip_jr.tripJr.jooq.Public

import java.util.UUID

import org.jooq.Parameter
import org.jooq.impl.AbstractRoutine
import org.jooq.impl.Internal
import org.jooq.impl.SQLDataType


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class UuidGenerateV4 : AbstractRoutine<UUID>("uuid_generate_v4", Public.PUBLIC, SQLDataType.UUID) {
    companion object {

        /**
         * The parameter <code>public.uuid_generate_v4.RETURN_VALUE</code>.
         */
        val RETURN_VALUE: Parameter<UUID?> = Internal.createParameter("RETURN_VALUE", SQLDataType.UUID, false, false)
    }

    init {
        returnParameter = UuidGenerateV4.RETURN_VALUE
    }
}
