package com.example.gouni_mobile_application.domain.usecase.reservation

import com.example.gouni_mobile_application.domain.repository.ReservationRepository

class GetReservationsUseCase(
    private val reservationRepository: ReservationRepository
) {
    operator fun invoke(driverId: String) = reservationRepository.getReservations(driverId)
}