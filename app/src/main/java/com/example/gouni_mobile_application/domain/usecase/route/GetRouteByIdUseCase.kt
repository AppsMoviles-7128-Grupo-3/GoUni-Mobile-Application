package com.example.gouni_mobile_application.domain.usecase.route

import com.example.gouni_mobile_application.domain.model.Route
import com.example.gouni_mobile_application.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow

class GetRouteByIdUseCase(
    private val routeRepository: RouteRepository
) {
    operator fun invoke(routeId: String): Flow<Route?> {
        return routeRepository.getRouteById(routeId)
    }
} 