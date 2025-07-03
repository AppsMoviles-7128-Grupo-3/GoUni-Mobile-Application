package com.example.gouni_mobile_application.data.remote.dto

data class ReservationDto(
    val id: Long? = null,
    val routeId: Long,
    val passengerId: Long,
    val studentName: String,
    val age: Int,
    val meetingPlace: String,
    val universityId: String,
    val profilePhoto: String?,
    val status: String
) 