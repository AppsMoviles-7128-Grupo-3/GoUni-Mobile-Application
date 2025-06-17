package com.example.gouni_mobile_application.domain.usecase.auth

import com.example.gouni_mobile_application.domain.repository.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String, university: String) =
        authRepository.register(name, email, password, university)
}