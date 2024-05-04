package com.trip_jr.tripJr.config

import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime


@Configuration
class DateTimeScalarConfig {


    @Bean
    fun dateTimeScalar(): GraphQLScalarType {
        return GraphQLScalarType.newScalar()
            .name("DateTime")
            .description("Date and time value")
            .coercing(object : Coercing<LocalDateTime, String> {
                @Deprecated("Deprecated in Java")
                override fun parseValue(input: Any): LocalDateTime? {
                    // Parse input value (e.g., from a query variable)
                    return LocalDateTime.parse(input.toString())
                }

                @Deprecated("Deprecated in Java")
                override fun parseLiteral(input: Any): LocalDateTime? {
                    // Parse input literal (e.g., from a query string)
                    return LocalDateTime.parse(input.toString())
                }

                @Deprecated("Deprecated in Java")
                override fun serialize(dataFetcherResult: Any): String? {
                    // Serialize data fetcher result (e.g., for response)
                    return dataFetcherResult.toString()
                }
            })
            .build()
    }
}