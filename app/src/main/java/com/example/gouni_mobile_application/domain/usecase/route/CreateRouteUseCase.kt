package com.example.gouni_mobile_application.domain.usecase.route

import com.example.gouni_mobile_application.domain.model.Route
import com.example.gouni_mobile_application.domain.repository.RouteRepository

class CreateRouteUseCase(
    private val routeRepository: RouteRepository
) {
    suspend operator fun invoke(route: Route) = routeRepository.createRoute(route)
}