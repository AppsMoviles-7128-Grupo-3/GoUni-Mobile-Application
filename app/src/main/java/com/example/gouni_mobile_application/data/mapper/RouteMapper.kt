package com.example.gouni_mobile_application.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.gouni_mobile_application.data.local.entity.RouteEntity
import com.example.gouni_mobile_application.domain.model.Route
import java.time.DayOfWeek
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
fun RouteEntity.toDomain(): Route = Route(
    id = id,
    driverId = driverId,
    carId = carId,
    start = start,
    end = end,
    days = days.split(",").map { DayOfWeek.valueOf(it) },
    departureTime = LocalTime.parse(departureTime),
    arrivalTime = LocalTime.parse(arrivalTime),
    availableSeats = availableSeats,
    price = price
)

fun Route.toEntity(): RouteEntity = RouteEntity(
    id = if (id.isEmpty()) java.util.UUID.randomUUID().toString() else id,
    driverId = driverId,
    carId = carId,
    start = start,
    end = end,
    days = days.joinToString(",") { it.name },
    departureTime = departureTime.toString(),
    arrivalTime = arrivalTime.toString(),
    availableSeats = availableSeats,
    price = price
)