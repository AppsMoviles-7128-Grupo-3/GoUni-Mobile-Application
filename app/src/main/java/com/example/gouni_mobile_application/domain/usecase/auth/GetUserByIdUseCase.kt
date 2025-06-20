package com.example.gouni_mobile_application.domain.usecase.auth

import com.example.gouni_mobile_application.domain.model.User
import com.example.gouni_mobile_application.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class GetUserByIdUseCase(private val repository: AuthRepository) {
    operator fun invoke(userId: String): Flow<User?> {
        return repository.getUserByIdFlow(userId)
    }
} 