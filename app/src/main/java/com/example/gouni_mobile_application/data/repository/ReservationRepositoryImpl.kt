package com.example.gouni_mobile_application.data.repository

import com.example.gouni_mobile_application.data.remote.api.ReservationApi
import com.example.gouni_mobile_application.data.remote.dto.ReservationDto
import com.example.gouni_mobile_application.domain.model.StudentReservation
import com.example.gouni_mobile_application.domain.model.ReservationStatus
import com.example.gouni_mobile_application.domain.repository.ReservationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReservationRepositoryImpl(
    private val reservationApi: ReservationApi
) : ReservationRepository {
    override fun getReservations(driverId: String): Flow<List<StudentReservation>> = flow {
        // Assuming backend provides a way to get reservations by driverId, otherwise adjust as needed
        // Here, we fetch all routes by driver and then all reservations for those routes
        // This is a placeholder; you may need to implement this logic in your backend or adjust here
        emit(emptyList())
    }

    override suspend fun updateReservationStatus(reservationId: String, status: ReservationStatus): Result<Unit> = withContext(Dispatchers.IO) {
        val response = reservationApi.updateStatus(reservationId.toLong(), status.name)
        if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(response.message()))
        }
    }

    // Keep the other methods for direct API usage if needed
    fun getReservationsByRoute(routeId: String): Flow<List<StudentReservation>> = flow {
        val response = reservationApi.getByRouteId(routeId.toLong())
        if (response.isSuccessful) {
            emit(response.body()?.map { it.toDomain() } ?: emptyList())
        } else {
            emit(emptyList())
        }
    }

    fun getReservationsByPassenger(passengerId: String): Flow<List<StudentReservation>> = flow {
        val response = reservationApi.getByPassengerId(passengerId.toLong())
        if (response.isSuccessful) {
            emit(response.body()?.map { it.toDomain() } ?: emptyList())
        } else {
            emit(emptyList())
        }
    }

    suspend fun createReservation(reservation: StudentReservation) {
        reservationApi.create(reservation.toDto())
    }
}

// Extension functions for mapping
private fun ReservationDto.toDomain() = StudentReservation(
    id = id?.toString() ?: "",
    routeId = routeId.toString(),
    studentName = studentName,
    age = age,
    meetingPlace = meetingPlace,
    universityId = universityId,
    profilePhoto = profilePhoto,
    status = try { ReservationStatus.valueOf(status) } catch (e: Exception) { ReservationStatus.PENDING }
)

private fun StudentReservation.toDto() = ReservationDto(
    id = id.toLongOrNull(),
    routeId = routeId.toLong(),
    passengerId = 0L, // Provide a default value if not available
    studentName = studentName,
    age = age,
    meetingPlace = meetingPlace,
    universityId = universityId,
    profilePhoto = profilePhoto,
    status = status.name
)