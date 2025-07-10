package com.example.gouni_mobile_application.domain.usecase.route

import com.example.gouni_mobile_application.domain.repository.RouteRepository

class GetMyRoutesUseCase(
    private val routeRepository: RouteRepository
) {
    operator fun invoke(userId: String) = routeRepository.getMyRoutes(userId)
}