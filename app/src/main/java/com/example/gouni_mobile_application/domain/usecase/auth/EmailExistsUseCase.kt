package com.example.gouni_mobile_application.domain.usecase.auth

import com.example.gouni_mobile_application.domain.repository.AuthRepository

class EmailExistsUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Boolean {
        return authRepository.emailExists(email)
    }
} 