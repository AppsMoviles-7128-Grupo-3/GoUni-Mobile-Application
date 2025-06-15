package com.example.gouni_mobile_application.domain.repository

import com.example.gouni_mobile_application.domain.models.Route

interface RouteRepository {
    suspend fun saveRoute(route: Route)
    suspend fun getRoutes(): List<Route>
}