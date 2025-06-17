package com.example.gouni_mobile_application.domain.usecase.auth

import com.example.gouni_mobile_application.domain.repository.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.logout()
}