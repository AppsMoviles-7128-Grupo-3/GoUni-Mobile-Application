package com.example.gouni_mobile_application.data.repository

import com.example.gouni_mobile_application.data.remote.api.RouteApi
import com.example.gouni_mobile_application.data.remote.dto.RouteDto
import com.example.gouni_mobile_application.domain.model.Route
import com.example.gouni_mobile_application.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RouteRepositoryImpl(
    private val routeApi: RouteApi
) : RouteRepository {
    override fun getMyRoutes(userId: String): Flow<List<Route>> = flow {
        val response = routeApi.getByUserId(userId.toLong())
        if (response.isSuccessful) {
            emit(response.body()?.map { dto -> dto.toDomain() } ?: emptyList())
        } else {
            emit(emptyList())
        }
    }

    override suspend fun createRoute(route: Route): Result<String> {
        val response = routeApi.create(route.toDto())
        return if (response.isSuccessful) {
            val id = response.body()?.id?.toString() ?: ""
            Result.success(id)
        } else {
            val errorBody = response.errorBody()?.string()
            Result.failure(Exception("HTTP ${response.code()}: ${response.message()} - $errorBody"))
        }
    }

    override suspend fun deleteRoute(routeId: String): Result<Unit> {
        val response = routeApi.delete(routeId.toLong())
        return if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(response.message()))
        }
    }

    override fun getRouteById(routeId: String): Flow<Route?> = flow {
        val response = routeApi.getById(routeId.toLong())
        if (response.isSuccessful) {
            emit(response.body()?.toDomain())
        } else {
            emit(null)
        }
    }
}

// Extension functions for mapping
private fun RouteDto.toDomain() = Route(
    id = id?.toString() ?: "",
    userId = userId.toString(),
    carId = carId.toString(),
    start = start,
    end = end,
    days = days.map { java.time.DayOfWeek.valueOf(it) },
    departureTime = java.time.LocalTime.parse(departureTime),
    arrivalTime = java.time.LocalTime.parse(arrivalTime),
    availableSeats = availableSeats,
    price = price
)

private fun Route.toDto() = RouteDto(
    id = id.toLongOrNull(),
    userId = userId.toLong(),
    carId = carId.toLong(),
    start = start,
    end = end,
    days = days.map { it.name },
    departureTime = departureTime.toString(),
    arrivalTime = arrivalTime.toString(),
    availableSeats = availableSeats,
    price = price
)