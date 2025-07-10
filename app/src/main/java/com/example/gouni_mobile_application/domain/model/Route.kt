package com.example.gouni_mobile_application.domain.model

import java.time.DayOfWeek
import java.time.LocalTime

data class Route(
    val id: String = "",
    val userId: String,
    val carId: String,
    val start: String,
    val end: String,
    val days: List<DayOfWeek>,
    val departureTime: LocalTime,
    val arrivalTime: LocalTime,
    val availableSeats: Int,
    val price: Double
)