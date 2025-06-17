package com.example.gouni_mobile_application.domain.repository

import com.example.gouni_mobile_application.domain.model.Route
import kotlinx.coroutines.flow.Flow

interface RouteRepository {
    suspend fun createRoute(route: Route): Result<String>
    fun getMyRoutes(driverId: String): Flow<List<Route>>
    suspend fun deleteRoute(routeId: String): Result<Unit>
}