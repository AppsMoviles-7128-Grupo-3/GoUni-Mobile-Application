package com.example.gouni_mobile_application.domain.repository

import com.example.gouni_mobile_application.domain.model.StudentReservation
import kotlinx.coroutines.flow.Flow

interface ReservationRepository {
    fun getReservationsByRoute(routeId: String): Flow<List<StudentReservation>>
    fun getReservationsByPassenger(passengerId: String): Flow<List<StudentReservation>>
    suspend fun createReservation(reservation: StudentReservation)
    fun getReservationsByDriver(driverId: String): Flow<List<StudentReservation>>
}