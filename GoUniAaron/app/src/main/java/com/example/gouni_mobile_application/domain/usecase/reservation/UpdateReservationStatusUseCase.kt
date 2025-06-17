package com.example.gouni_mobile_application.domain.usecase.reservation

import com.example.gouni_mobile_application.domain.model.ReservationStatus
import com.example.gouni_mobile_application.domain.repository.ReservationRepository

class UpdateReservationStatusUseCase(
    private val reservationRepository: ReservationRepository
) {
    suspend operator fun invoke(reservationId: String, status: ReservationStatus) =
        reservationRepository.updateReservationStatus(reservationId, status)
}