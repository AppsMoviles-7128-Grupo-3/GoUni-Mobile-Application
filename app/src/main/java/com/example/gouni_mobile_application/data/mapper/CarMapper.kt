package com.example.gouni_mobile_application.data.mapper

import com.example.gouni_mobile_application.data.local.entity.CarEntity
import com.example.gouni_mobile_application.domain.model.Car

fun CarEntity.toCar(): Car {
    return Car(
        id = id,
        userId = userId,
        make = make,
        model = model,
        licensePlate = licensePlate,
        color = color,
        year = year,
        insuranceInfo = insuranceInfo,
        insuranceBrand = insuranceBrand,
        registrationNumber = registrationNumber
    )
}

fun Car.toCarEntity(): CarEntity {
    return CarEntity(
        id = id,
        userId = userId,
        make = make,
        model = model,
        licensePlate = licensePlate,
        color = color,
        year = year,
        insuranceInfo = insuranceInfo,
        insuranceBrand = insuranceBrand,
        registrationNumber = registrationNumber
    )
} 