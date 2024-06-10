package com.trip_jr.tripJr.config

import com.trip_jr.tripJr.fetchers.CreateHotelDataFetcher
import graphql.kickstart.servlet.apollo.ApolloScalars
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
    fun runtimeWiringConfigurer(uuidScalar: GraphQLScalarType, dateScalar: GraphQLScalarType, dateTimeScalar: GraphQLScalarType, createHotelDataFetcher: CreateHotelDataFetcher): RuntimeWiringConfigurer {
        return RuntimeWiringConfigurer { wiringBuilder: RuntimeWiring.Builder ->
            wiringBuilder.scalar(uuidScalar)
            wiringBuilder.scalar(dateTimeScalar)
            wiringBuilder.scalar(ApolloScalars.Upload)
            wiringBuilder.type("Mutation") { typeWiring ->
                typeWiring.dataFetcher("createHotel", createHotelDataFetcher)
            }
            wiringBuilder.scalar(dateScalar).build()
        }
    }

}