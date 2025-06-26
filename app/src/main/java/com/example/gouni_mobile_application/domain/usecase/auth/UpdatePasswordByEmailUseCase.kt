package com.example.gouni_mobile_application.domain.usecase.auth

import com.example.gouni_mobile_application.domain.repository.AuthRepository

class UpdatePasswordByEmailUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, newPassword: String) =
        authRepository.updatePasswordByEmail(email, newPassword)
} 