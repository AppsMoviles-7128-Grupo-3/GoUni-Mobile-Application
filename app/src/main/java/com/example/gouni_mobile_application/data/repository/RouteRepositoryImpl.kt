package com.example.gouni_mobile_application.data.repository

import com.example.gouni_mobile_application.data.local.dao.RouteDao
import com.example.gouni_mobile_application.data.mapper.toDomain
import com.example.gouni_mobile_application.data.mapper.toEntity
import com.example.gouni_mobile_application.domain.model.Route
import com.example.gouni_mobile_application.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import java.time.LocalTime

class RouteRepositoryImpl(
    private val routeDao: RouteDao
) : RouteRepository {

    override suspend fun createRoute(route: Route): Result<String> {
        return try {
            val entity = route.toEntity()
            routeDao.insertRoute(entity)
            Result.success(entity.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getMyRoutes(driverId: String): Flow<List<Route>> {
        return routeDao.getRoutesByDriver(driverId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun deleteRoute(routeId: String): Result<Unit> {
        return try {
            routeDao.deleteRoute(routeId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getRouteById(routeId: String): Flow<Route?> {
        val route = getMockRoutes().find { it.id == routeId }
        return flowOf(route)
    }

    private fun getMockRoutes(): List<Route> {
        return listOf(
            Route(
                id = "route1",
                driverId = "user1",
                carId = "car1",
                start = "San Isidro",
                end = "UPC Sede San Isidro",
                days = listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY),
                departureTime = LocalTime.of(7, 30),
                arrivalTime = LocalTime.of(8, 0),
                availableSeats = 4,
                price = 5.0
            ),
            Route(
                id = "route2",
                driverId = "user1",
                carId = "car1",
                start = "Miraflores",
                end = "UPC Sede Monterrico",
                days = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
                departureTime = LocalTime.of(8, 0),
                arrivalTime = LocalTime.of(8, 30),
                availableSeats = 3,
                price = 6.0
            )
        )
    }
}