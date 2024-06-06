package com.trip_jr.tripJr

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties
class TripJrApplication

fun main(args: Array<String>) {
	runApplication<TripJrApplication>(*args)
}