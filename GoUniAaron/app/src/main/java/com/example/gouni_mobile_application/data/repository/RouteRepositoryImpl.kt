package com.example.gouni_mobile_application.data.repository

import com.example.gouni_mobile_application.data.local.dao.RouteDao
import com.example.gouni_mobile_application.data.mapper.toDomain
import com.example.gouni_mobile_application.data.mapper.toEntity
import com.example.gouni_mobile_application.domain.model.Route
import com.example.gouni_mobile_application.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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
}