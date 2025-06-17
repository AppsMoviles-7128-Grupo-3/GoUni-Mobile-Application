package com.example.gouni_mobile_application.data.repository

import com.example.gouni_mobile_application.domain.model.ReservationStatus
import com.example.gouni_mobile_application.domain.model.StudentReservation
import com.example.gouni_mobile_application.domain.repository.ReservationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ReservationRepositoryImpl : ReservationRepository {

    override fun getReservations(driverId: String): Flow<List<StudentReservation>> {
        return flowOf(getMockReservations())
    }

    override suspend fun updateReservationStatus(reservationId: String, status: ReservationStatus): Result<Unit> {
        return try {
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getMockReservations(): List<StudentReservation> {
        return listOf(
            StudentReservation(
                id = "1",
                routeId = "route1",
                studentName = "Ana García",
                age = 20,
                meetingPlace = "Plaza San Martín",
                universityId = "U20201234",
                status = ReservationStatus.PENDING
            ),
            StudentReservation(
                id = "2",
                routeId = "route1",
                studentName = "Carlos López",
                age = 22,
                meetingPlace = "Estación Central",
                universityId = "U20195678",
                status = ReservationStatus.ACCEPTED
            ),
            StudentReservation(
                id = "3",
                routeId = "route2",
                studentName = "María Rodríguez",
                age = 19,
                meetingPlace = "Centro Comercial",
                universityId = "U20212345",
                status = ReservationStatus.PENDING
            )
        )
    }
}