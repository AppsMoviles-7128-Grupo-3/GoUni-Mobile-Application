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
            // TODO: Implement backend call to update reservation status
            // This would typically make an API call to update the reservation status
            // For now, we just return success for demo purposes
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getMockReservations(): List<StudentReservation> {
        return listOf(
            StudentReservation(
                id = "res_001",
                routeId = "route1", // This would be the actual route ID from the backend
                studentName = "María García López",
                age = 20,
                meetingPlace = "Plaza Mayor, San Isidro",
                universityId = "U20201234",
                universityName = "UPC",
                profilePhoto = null, // Would be a URL from the backend
                status = ReservationStatus.PENDING // This would be the actual status from backend
            )
        )
    }
}