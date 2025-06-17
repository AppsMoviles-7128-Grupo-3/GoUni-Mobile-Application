package com.example.gouni_mobile_application.domain.usecase.auth

import com.example.gouni_mobile_application.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        authRepository.login(email, password)
}