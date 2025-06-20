package com.example.gouni_mobile_application.domain.usecase.car

import com.example.gouni_mobile_application.domain.repository.CarRepository

class HasCarUseCase(
    private val carRepository: CarRepository
) {
    suspend operator fun invoke(userId: String): Boolean {
        return carRepository.hasCar(userId)
    }
} 