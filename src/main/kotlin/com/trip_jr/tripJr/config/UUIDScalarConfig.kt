package com.trip_jr.tripJr.config

import graphql.language.StringValue
import graphql.schema.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class UUIDScalarConfig {

    @Bean
    fun uuidScalar(): GraphQLScalarType {
        return GraphQLScalarType.newScalar()
            .name("UUID")
            .description("A custom scalar that handles UUIDs")
            .coercing(object : Coercing<Any, Any> {
                @Deprecated("Deprecated in Java")
                override fun serialize(dataFetcherResult: Any): Any {
                    return serializeUUID(dataFetcherResult)
                }

                @Deprecated("Deprecated in Java")
                override fun parseValue(input: Any): Any {
                    return parseUUIDFromVariable(input)
                }

                @Deprecated("Deprecated in Java")
                override fun parseLiteral(input: Any): Any {
                    return parseUUIDFromAstLiteral(input)
                }
            })
            .build()
    }

    private fun serializeUUID(dataFetcherResult: Any): Any {
        val possibleUUIDValue = dataFetcherResult.toString()
        try {
            return UUID.fromString(possibleUUIDValue)
        } catch (e: IllegalArgumentException) {
            throw CoercingSerializeException("Unable to serialize $possibleUUIDValue as a UUID")
        }
    }

    private fun parseUUIDFromVariable(input: Any): Any {
        if (input is String) {
            val possibleUUIDValue = input.toString()
            try {
                return UUID.fromString(possibleUUIDValue)
            } catch (e: IllegalArgumentException) {
                throw CoercingParseValueException("Unable to parse variable value $input as a UUID")
            }
        }
        throw CoercingParseValueException("Unable to parse variable value $input as a UUID")
    }

    private fun parseUUIDFromAstLiteral(input: Any): Any {
        if (input is StringValue) {
            val possibleUUIDValue = input.value
            try {
                return UUID.fromString(possibleUUIDValue)
            } catch (e: IllegalArgumentException) {
                throw CoercingParseLiteralException("Value is not a valid UUID: '$possibleUUIDValue'")
            }
        }
        throw CoercingParseLiteralException("Value is not a valid UUID: '$input'")
    }
}