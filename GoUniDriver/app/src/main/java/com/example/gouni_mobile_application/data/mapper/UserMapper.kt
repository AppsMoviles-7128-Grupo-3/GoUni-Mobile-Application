package com.example.gouni_mobile_application.data.mapper

import com.example.gouni_mobile_application.data.local.entity.UserEntity
import com.example.gouni_mobile_application.domain.model.User

fun UserEntity.toDomain(): User = User(
    id = id,
    name = name,
    email = email,
    university = university
)

fun User.toEntity(password: String): UserEntity = UserEntity(
    id = id,
    name = name,
    email = email,
    password = password,
    university = university
)