package com.example.gouni_mobile_application.domain.repository

import com.example.gouni_mobile_application.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(name: String, email: String, password: String, university: String): Result<User>
    suspend fun getCurrentUser(): User?
    suspend fun logout()
}