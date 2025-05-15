package com.example.gouni_mobile_application.data.repository

import com.example.gouni_mobile_application.data.models.NewUser
import com.example.gouni_mobile_application.data.models.UserCredentials
import com.example.gouni_mobile_application.data.models.UserDTO
import com.example.gouni_mobile_application.data.remote.AuthService

class AuthRepositoryImpl (private val authService: AuthService) {
    fun login(credentials: UserCredentials): Result<UserDTO> {
        return authService.login(credentials)
    }

    fun register(newUser: NewUser): Boolean {
        return authService.register(newUser)
    }
}