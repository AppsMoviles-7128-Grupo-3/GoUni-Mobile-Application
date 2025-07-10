package com.example.gouni_mobile_application.domain.repository

import com.example.gouni_mobile_application.data.remote.api.Location

interface MapRepository {
    suspend fun getCoordinates(place: String): Location?
    suspend fun getRoutePolyline(origin: Location, destination: Location): String?
} 