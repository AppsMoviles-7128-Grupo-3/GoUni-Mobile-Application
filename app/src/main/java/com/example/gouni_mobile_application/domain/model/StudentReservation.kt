package com.example.gouni_mobile_application.domain.model

data class StudentReservation(
    val id: String,
    val routeId: String,
    val studentName: String,
    val age: Int,
    val meetingPlace: String,
    val universityId: String,
    val universityName: String = "",
    val profilePhoto: String? = null,
    val status: ReservationStatus = ReservationStatus.PENDING
)

enum class ReservationStatus {
    PENDING, ACCEPTED, REJECTED
}