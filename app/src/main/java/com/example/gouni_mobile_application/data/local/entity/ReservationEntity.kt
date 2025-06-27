package com.example.gouni_mobile_application.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservations")
data class ReservationEntity(
    @PrimaryKey val id: String,
    val routeId: String,
    val studentName: String,
    val age: Int,
    val meetingPlace: String,
    val universityId: String,
    val universityName: String = "",
    val profilePhoto: String?,
    val status: String
)