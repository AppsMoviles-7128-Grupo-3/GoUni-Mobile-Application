package com.example.gouni_mobile_application.data.remote.dto

import java.time.LocalTime

data class RouteDto(
    val id: Long? = null,
    val driverId: Long,
    val carId: Long,
    val start: String,
    val end: String,
    val days: List<String>,
    val departureTime: String, // Use String for time to match JSON
    val arrivalTime: String,
    val availableSeats: Int,
    val price: Double
) 