package com.example.gouni_mobile_application.data.mapper

import com.example.gouni_mobile_application.data.local.entity.ReservationEntity
import com.example.gouni_mobile_application.domain.model.ReservationStatus
import com.example.gouni_mobile_application.domain.model.StudentReservation

fun ReservationEntity.toDomain(): StudentReservation = StudentReservation(
    id = id,
    routeId = routeId,
    studentName = studentName,
    age = age,
    meetingPlace = meetingPlace,
    universityId = universityId,
    universityName = universityName,
    profilePhoto = profilePhoto,
    status = ReservationStatus.valueOf(status)
)

fun StudentReservation.toEntity(): ReservationEntity = ReservationEntity(
    id = id,
    routeId = routeId,
    studentName = studentName,
    age = age,
    meetingPlace = meetingPlace,
    universityId = universityId,
    universityName = universityName,
    profilePhoto = profilePhoto,
    status = status.name
)