package com.example.gouni_mobile_application.domain.usecase.car

import com.example.gouni_mobile_application.domain.model.Car
import com.example.gouni_mobile_application.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow

class GetCarUseCase(
    private val carRepository: CarRepository
) {
    operator fun invoke(userId: String): Flow<Car?> {
        return carRepository.getCarByUserId(userId)
    }
} 