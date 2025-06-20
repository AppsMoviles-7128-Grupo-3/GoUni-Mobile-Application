package com.example.gouni_mobile_application.domain.usecase.car

import com.example.gouni_mobile_application.domain.model.Car
import com.example.gouni_mobile_application.domain.repository.CarRepository

class InsertCarUseCase(
    private val carRepository: CarRepository
) {
    suspend operator fun invoke(car: Car) {
        carRepository.insertCar(car)
    }
} 