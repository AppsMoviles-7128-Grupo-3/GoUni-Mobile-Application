package com.example.gouni_mobile_application.data.local

import com.example.gouni_mobile_application.data.models.RouteEntity

interface RouteLocalDataSource {
    suspend fun saveRoute(route: RouteEntity)
    suspend fun getRoutes(): List<RouteEntity>
}