package com.example.gouni_mobile_application.domain.usecase.route

import com.example.gouni_mobile_application.domain.repository.RouteRepository

class DeleteRouteUseCase(
    private val routeRepository: RouteRepository
) {
    suspend operator fun invoke(routeId: String) = routeRepository.deleteRoute(routeId)
}