package com.example.gouni_mobile_application.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routes")
data class RouteEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val carId: String,
    val start: String,
    val end: String,
    val days: String,
    val departureTime: String,
    val arrivalTime: String,
    val availableSeats: Int,
    val price: Double
)