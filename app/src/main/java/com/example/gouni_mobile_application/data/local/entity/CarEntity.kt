package com.example.gouni_mobile_application.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class CarEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val make: String,
    val model: String,
    val licensePlate: String,
    val color: String,
    val year: Int,
    val insuranceInfo: String,
    val insuranceBrand: String,
    val registrationNumber: String
) 