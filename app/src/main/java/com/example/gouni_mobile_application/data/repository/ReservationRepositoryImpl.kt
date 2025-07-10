package com.example.gouni_mobile_application.data.repository

import com.example.gouni_mobile_application.data.remote.api.ReservationApi
import com.example.gouni_mobile_application.data.remote.dto.ReservationDto
import com.example.gouni_mobile_application.domain.model.StudentReservation
import com.example.gouni_mobile_application.domain.repository.ReservationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ReservationRepositoryImpl(
    private val reservationApi: ReservationApi
) : ReservationRepository {
    override fun getReservationsByRoute(routeId: String): Flow<List<StudentReservation>> = flow {
        val response = reservationApi.getByRouteId(routeId.toLong())
        if (response.isSuccessful) {
            emit(response.body()?.map { it.toDomain() } ?: emptyList())
        } else {
            emit(emptyList())
        }
    }

    override fun getReservationsByPassenger(passengerId: String): Flow<List<StudentReservation>> = flow {
        val response = reservationApi.getByPassengerId(passengerId.toLong())
        if (response.isSuccessful) {
            emit(response.body()?.map { it.toDomain() } ?: emptyList())
        } else {
            emit(emptyList())
        }
    }

    override suspend fun createReservation(reservation: StudentReservation) {
        reservationApi.create(reservation.toDto())
    }

    override fun getReservationsByDriver(driverId: String): Flow<List<StudentReservation>> = flow {
        val response = reservationApi.getByDriverId(driverId.toLong())
        if (response.isSuccessful) {
            emit(response.body()?.map { it.toDomain() } ?: emptyList())
        } else {
            emit(emptyList())
        }
    }
}

// Extension functions for mapping
private fun ReservationDto.toDomain() = StudentReservation(
    id = id?.toString() ?: "",
    routeId = routeId.toString(),
    passengerId = passengerId.toString()
)

private fun StudentReservation.toDto() = ReservationDto(
    id = id.toLongOrNull(),
    routeId = routeId.toLong(),
    passengerId = passengerId.toLong()
)