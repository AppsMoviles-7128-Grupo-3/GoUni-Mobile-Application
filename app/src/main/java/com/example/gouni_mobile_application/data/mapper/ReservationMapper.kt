package com.example.gouni_mobile_application.data.mapper

import com.example.gouni_mobile_application.data.local.entity.ReservationEntity
import com.example.gouni_mobile_application.domain.model.StudentReservation

fun ReservationEntity.toDomain(): StudentReservation = StudentReservation(
    id = id,
    routeId = routeId,
    passengerId = passengerId
)

fun StudentReservation.toEntity(): ReservationEntity = ReservationEntity(
    id = id,
    routeId = routeId,
    passengerId = passengerId
)