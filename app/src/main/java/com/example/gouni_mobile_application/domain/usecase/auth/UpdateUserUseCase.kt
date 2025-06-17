package com.example.gouni_mobile_application.domain.usecase.auth

import com.example.gouni_mobile_application.domain.model.User
import com.example.gouni_mobile_application.domain.repository.AuthRepository

class UpdateUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(user: User, password: String) =
        authRepository.updateUser(user, password)
}