package com.trip_jr.tripJr.config
import graphql.language.StringValue
import graphql.schema.*
import org.springframework.stereotype.Component
import java.util.UUID

@Component // Ensure it's a Spring-managed component
class UUIDCoercing : Coercing<UUID, String> {

    // Convert input value to UUID
     fun parseValue(input: Any?): UUID {
        if (input == null) {
            throw CoercingParseValueException("Input value cannot be null")
        }
        if (input !is String) {
            throw CoercingParseValueException("Expected UUID input to be String but was ${input.javaClass.simpleName}")
        }
        try {
            return UUID.fromString(input)
        } catch (e: IllegalArgumentException) {
            throw CoercingParseValueException("Invalid UUID format: $input")
        }
    }

    // Convert AST literal to UUID
     fun parseLiteral(input: Any?): UUID {
        if (input !is StringValue) {
            throw CoercingParseLiteralException("Expected AST type 'StringValue' but was '${input?.javaClass?.simpleName}'")
        }
        try {
            return UUID.fromString(input.value)
        } catch (e: IllegalArgumentException) {
            throw CoercingParseLiteralException("Invalid UUID format: ${input.value}")
        }
    }

    // Convert UUID to output value
     fun serialize(dataFetcherResult: Any?): String {
        if (dataFetcherResult == null) {
            throw CoercingSerializeException("Data fetcher result cannot be null")
        }
        if (dataFetcherResult !is UUID) {
            throw CoercingSerializeException("Expected UUID result but was '${dataFetcherResult.javaClass.simpleName}'")
        }
        return dataFetcherResult.toString()
    }
}