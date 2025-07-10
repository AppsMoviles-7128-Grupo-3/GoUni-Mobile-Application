package com.example.gouni_mobile_application.domain.usecase.reservation

import com.example.gouni_mobile_application.domain.repository.ReservationRepository

class GetReservationsByDriverUseCase(private val reservationRepository: ReservationRepository) {
    operator fun invoke(driverId: String) = reservationRepository.getReservationsByDriver(driverId)
} 