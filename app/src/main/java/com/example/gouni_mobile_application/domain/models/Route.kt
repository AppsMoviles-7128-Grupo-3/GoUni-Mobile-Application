package com.example.gouni_mobile_application.domain.models

import java.time.DayOfWeek
import java.time.LocalTime

data class Route(
    val start: String,
    val end: String,
    val days: List<DayOfWeek>,
    val departureTime: LocalTime,
    val arrivalTime: LocalTime
)
