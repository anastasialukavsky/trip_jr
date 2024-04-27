package com.trip_jr.tripJr.config

import graphql.schema.GraphQLScalarType
import graphql.schema.idl.RuntimeWiring
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer




@Configuration
class GraphQLConfig {

//    @Bean
//    fun schemaParserOptions(): SchemaParserOptions {
//        return SchemaParserOptions.newOptions()
//            .scalars(ExtendedScalars.UUID) // Register UUID scalar globally
//            .build()
//    }

    @Bean
    fun uuidScalarConfig(): UUIDScalarConfig {
        return UUIDScalarConfig()
    }
    @Bean
    fun runtimeWiringConfigurer(uuidScalar: GraphQLScalarType, dateScalar: GraphQLScalarType): RuntimeWiringConfigurer {
        return RuntimeWiringConfigurer { wiringBuilder: RuntimeWiring.Builder ->
            wiringBuilder.scalar(uuidScalar)
            wiringBuilder.scalar(dateScalar).build()
        }
    }

}