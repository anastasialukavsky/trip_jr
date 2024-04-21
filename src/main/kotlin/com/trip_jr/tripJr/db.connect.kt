package com.trip_jr.tripJr


import java.sql.DriverManager

fun main() {
    val userName = "postgres"
    val password = "postgrespass"
    val url = "jdbc:postgresql://localhost:5432/tripjr"

    // Connection is the only JDBC resource that we need
    // PreparedStatement and ResultSet are handled by jOOQ, internally
    try {
        DriverManager.getConnection(url, userName, password).use { conn ->
            // Your code for database operations here
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}