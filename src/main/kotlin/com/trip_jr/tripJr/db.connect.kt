package com.trip_jr.tripJr


import java.sql.DriverManager

fun main() {
    val userName = "postgres"
    val password = "postgrespass"
    val url = "jdbc:postgresql://localhost:5432/tripjr"


    try {
        DriverManager.getConnection(url, userName, password).use { conn ->

        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}