package com.example.gouni_mobile_application.data.repository

import com.example.gouni_mobile_application.data.local.dao.CarDao
import com.example.gouni_mobile_application.data.mapper.toCar
import com.example.gouni_mobile_application.data.mapper.toCarEntity
import com.example.gouni_mobile_application.domain.model.Car
import com.example.gouni_mobile_application.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CarRepositoryImpl(
    private val carDao: CarDao
) : CarRepository {

    override fun getCarByUserId(userId: String): Flow<Car?> {
        return carDao.getCarByUserId(userId).map { carEntity ->
            carEntity?.toCar()
        }
    }

    override suspend fun insertCar(car: Car) {
        carDao.insertCar(car.toCarEntity())
    }

    override suspend fun updateCar(car: Car) {
        carDao.updateCar(car.toCarEntity())
    }

    override suspend fun deleteCar(car: Car) {
        carDao.deleteCar(car.toCarEntity())
    }

    override suspend fun hasCar(userId: String): Boolean {
        return carDao.hasCar(userId)
    }
} 