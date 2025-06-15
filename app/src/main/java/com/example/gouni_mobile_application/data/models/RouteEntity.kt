package com.example.gouni_mobile_application.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gouni_mobile_application.domain.models.Route
import java.time.DayOfWeek
import java.time.LocalTime

@Entity(tableName = "routes")
data class RouteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val start: String,
    val end: String,
    val days: String,
    val departureTime: String,
    val arrivalTime: String
)

fun RouteEntity.toDomain(): Route {
    return Route(
        start = start,
        end = end,
        days = days.split(",").map { DayOfWeek.valueOf(it) },
        departureTime = LocalTime.parse(departureTime),
        arrivalTime = LocalTime.parse(arrivalTime)
    )
}

fun Route.toEntity(): RouteEntity {
    return RouteEntity(
        start = start,
        end = end,
        days = days.joinToString(",") { it.name },
        departureTime = departureTime.toString(),
        arrivalTime = arrivalTime.toString()
    )
}
