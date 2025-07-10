package com.example.gouni_mobile_application.domain.usecase.reservation

import com.example.gouni_mobile_application.domain.repository.ReservationRepository

class GetReservationsByRouteUseCase(
    private val reservationRepository: ReservationRepository
) {
    operator fun invoke(routeId: String) = reservationRepository.getReservationsByRoute(routeId)
}

class GetReservationsByPassengerUseCase(
    private val reservationRepository: ReservationRepository
) {
    operator fun invoke(passengerId: String) = reservationRepository.getReservationsByPassenger(passengerId)
}