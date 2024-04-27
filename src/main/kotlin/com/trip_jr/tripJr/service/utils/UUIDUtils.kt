package com.trip_jr.tripJr.service.utils

import org.springframework.stereotype.Component
import java.util.*

@Component
class UUIDUtils {

    fun generateUUID(): UUID {
        return UUID.randomUUID()
    }

}