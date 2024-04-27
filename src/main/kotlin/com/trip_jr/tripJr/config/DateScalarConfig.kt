package com.trip_jr.tripJr.config

import graphql.language.StringValue
import graphql.schema.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Configuration
class DateScalarConfig {

    @Bean
    fun dateScalar(): GraphQLScalarType {
        return GraphQLScalarType.newScalar()
            .name("Date")
            .description("A custom scalar that handles Date")
            .coercing(object : Coercing
            <Any, Any> {
                @Deprecated("Deprecated in Java")
                override fun serialize(dataFetcherResult: Any): Any {
                    try {
                        val publishedTime = dataFetcherResult as LocalDate
                        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
                        return formatter.format(publishedTime)
                    } catch (exception: Exception) {
                        throw CoercingSerializeException("Failed to serialize date: ${exception.message}")
                    }
                }


                @Deprecated("Deprecated in Java")
                override fun parseValue(input: Any): Any {
                    try {
                        val dateString = input.toString()
                        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
                        return LocalDate.parse(dateString, formatter)
                    } catch (exception: DateTimeParseException) {
                        throw CoercingParseValueException("Invalid date format: $input")
                    } catch (exception: Exception) {
                        throw CoercingParseValueException("Failed to parse date: ${exception.message}")
                    }
                }

                @Deprecated("Deprecated in Java")
                override fun parseLiteral(input: Any): Any {
                    try {
                        if (input is StringValue) {
                            val dateString = input.value
                            val formatter = DateTimeFormatter.ISO_LOCAL_DATE
                            return LocalDate.parse(dateString, formatter)
                        }
                        throw CoercingParseLiteralException("Invalid date format: $input")
                    } catch (exception: DateTimeParseException) {
                        throw CoercingParseLiteralException("Invalid date format: $input")
                    } catch (exception: Exception) {
                        throw CoercingParseLiteralException("Failed to parse date: ${exception.message}")
                    }
                }
            })
            .build()
    }
}