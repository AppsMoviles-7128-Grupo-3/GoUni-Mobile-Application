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
                studentName = "Jinx",
                age = 20,
                meetingPlace = "UTP",
                universityId = "U20201234",
                status = ReservationStatus.PENDING
            ),
            StudentReservation(
                id = "2",
                routeId = "route1",
                studentName = "Vi",
                age = 22,
                meetingPlace = "UPC Sede San Isidro",
                universityId = "U20201234",
                status = ReservationStatus.ACCEPTED
            ),
            StudentReservation(
                id = "3",
                routeId = "route2",
                studentName = "Ekko",
                age = 19,
                meetingPlace = "UPC Sede Monterrico",
                universityId = "U20201234",
                status = ReservationStatus.PENDING
            )
        )
    }
}