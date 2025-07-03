package com.example.gouni_mobile_application.data.remote.dto

data class CarDto(
    val id: Long? = null,
    val userId: Long,
    val make: String,
    val model: String,
    val licensePlate: String,
    val color: String,
    val year: Int,
    val insuranceInfo: String,
    val insuranceBrand: String,
    val registrationNumber: String
) 