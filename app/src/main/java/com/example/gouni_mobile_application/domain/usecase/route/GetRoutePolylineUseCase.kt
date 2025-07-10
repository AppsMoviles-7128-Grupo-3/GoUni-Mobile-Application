package com.example.gouni_mobile_application.domain.usecase.route

import com.example.gouni_mobile_application.domain.repository.MapRepository
import com.example.gouni_mobile_application.data.remote.api.Location

class GetRoutePolylineUseCase(private val mapRepository: MapRepository) {
    suspend fun getCoordinates(place: String): Location? =
        mapRepository.getCoordinates(place)

    suspend fun getRoutePolyline(origin: Location, destination: Location): String? =
        mapRepository.getRoutePolyline(origin, destination)
} 