package com.example.gouni_mobile_application.data.local

import com.example.gouni_mobile_application.data.models.RouteEntity

class RouteLocalDataSourceImpl(
    private val routesDao: RoutesDao
) : RouteLocalDataSource {

    override suspend fun saveRoute(route: RouteEntity) {
        routesDao.insert(route)
    }

    override suspend fun getRoutes(): List<RouteEntity> {
        return routesDao.getAllRoutes()
    }
}