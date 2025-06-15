package com.example.gouni_mobile_application.data.repository

import com.example.gouni_mobile_application.data.local.RouteLocalDataSource
import com.example.gouni_mobile_application.data.models.toDomain
import com.example.gouni_mobile_application.data.models.toEntity
import com.example.gouni_mobile_application.domain.models.Route
import com.example.gouni_mobile_application.domain.repository.RouteRepository

class RouteRepositoryImpl(
    private val localDataSource: RouteLocalDataSource
) : RouteRepository {

    override suspend fun saveRoute(route: Route) {
        val entity = route.toEntity()
        localDataSource.saveRoute(entity)
    }

    override suspend fun getRoutes(): List<Route> {
        return localDataSource.getRoutes().map { it.toDomain() }
    }
}