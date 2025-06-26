package com.example.gouni_mobile_application.domain.repository

import com.example.gouni_mobile_application.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(name: String, email: String, password: String, university: String, userCode: String): Result<User>
    suspend fun getCurrentUser(): User?
    suspend fun updateUser(user: User, password: String): Result<User>
    suspend fun logout()
    suspend fun getUserById(userId: String): User?
    fun getUserByIdFlow(userId: String): Flow<User?>
    suspend fun emailExists(email: String): Boolean
    suspend fun updatePasswordByEmail(email: String, newPassword: String): Result<Unit>
}