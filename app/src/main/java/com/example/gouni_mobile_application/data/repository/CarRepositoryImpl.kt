package com.example.gouni_mobile_application.data.repository

import com.example.gouni_mobile_application.data.remote.api.CarApi
import com.example.gouni_mobile_application.data.remote.dto.CarDto
import com.example.gouni_mobile_application.domain.model.Car
import com.example.gouni_mobile_application.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CarRepositoryImpl(
    private val carApi: CarApi
) : CarRepository {
    override fun getCarByUserId(userId: String): Flow<Car?> = flow {
        val response = carApi.getByUserId(userId.toLong())
        if (response.isSuccessful) {
            val cars = response.body()?.map { it.toDomain() } ?: emptyList()
            emit(cars.firstOrNull())
        } else {
            emit(null)
        }
    }

    override fun getCarById(id: String): Flow<Car?> = flow {
        val response = carApi.getById(id.toLong())
        if (response.isSuccessful) {
            emit(response.body()?.toDomain())
        } else {
            emit(null)
        }
    }

    override suspend fun insertCar(car: Car) {
        carApi.create(car.toDto())
    }

    override suspend fun updateCar(car: Car) {
        car.id?.toLongOrNull()?.let { carApi.update(it, car.toDto()) }
    }

    override suspend fun deleteCar(car: Car) {
        car.id?.toLongOrNull()?.let { carApi.delete(it) }
    }

    override suspend fun hasCar(userId: String): Boolean = withContext(Dispatchers.IO) {
        val response = carApi.getByUserId(userId.toLong())
        response.body()?.isNotEmpty() == true
    }
}

// Extension functions for mapping
private fun CarDto.toDomain() = Car(
    id = id?.toString() ?: "",
    userId = userId.toString(),
    make = make,
    model = model,
    licensePlate = licensePlate,
    color = color,
    year = year,
    insuranceInfo = insuranceInfo,
    insuranceBrand = insuranceBrand,
    registrationNumber = registrationNumber
)

private fun Car.toDto() = CarDto(
    id = id?.toLongOrNull(),
    userId = userId.toLong(),
    make = make,
    model = model,
    licensePlate = licensePlate,
    color = color,
    year = year,
    insuranceInfo = insuranceInfo,
    insuranceBrand = insuranceBrand,
    registrationNumber = registrationNumber
) 