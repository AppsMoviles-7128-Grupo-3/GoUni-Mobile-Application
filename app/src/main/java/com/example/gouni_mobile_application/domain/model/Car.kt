package com.example.gouni_mobile_application.domain.model

data class Car(
    val id: String,
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