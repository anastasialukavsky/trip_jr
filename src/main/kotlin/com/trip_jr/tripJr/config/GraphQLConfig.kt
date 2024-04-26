package com.trip_jr.tripJr.config

import graphql.schema.GraphQLScalarType
import graphql.schema.idl.RuntimeWiring
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer



@Configuration
class GraphQLConfig {
    @Bean
    fun uuidScalarConfig(): UUIDScalarConfig {
        return UUIDScalarConfig()
    }
    @Bean
    fun runtimeWiringConfigurer(uuidScalar: GraphQLScalarType): RuntimeWiringConfigurer {
        return RuntimeWiringConfigurer { wiringBuilder: RuntimeWiring.Builder ->
            wiringBuilder.scalar(uuidScalar)
        }
    }
}