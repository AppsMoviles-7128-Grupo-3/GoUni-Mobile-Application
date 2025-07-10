package com.example.gouni_mobile_application.domain.repository

import com.example.gouni_mobile_application.domain.model.Car
import kotlinx.coroutines.flow.Flow

interface CarRepository {
    fun getCarByUserId(userId: String): Flow<Car?>
    fun getCarById(id: String): Flow<Car?>
    suspend fun insertCar(car: Car)
    suspend fun updateCar(car: Car)
    suspend fun deleteCar(car: Car)
    suspend fun hasCar(userId: String): Boolean
} 