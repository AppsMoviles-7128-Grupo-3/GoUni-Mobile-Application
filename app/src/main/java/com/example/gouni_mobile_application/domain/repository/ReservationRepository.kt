package com.example.gouni_mobile_application.domain.repository

import com.example.gouni_mobile_application.domain.model.StudentReservation
import kotlinx.coroutines.flow.Flow

interface ReservationRepository {
    fun getReservations(driverId: String): Flow<List<StudentReservation>>
    suspend fun updateReservationStatus(reservationId: String, status: com.example.gouni_mobile_application.domain.model.ReservationStatus): Result<Unit>
}