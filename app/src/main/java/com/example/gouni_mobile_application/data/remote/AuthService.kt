package com.example.gouni_mobile_application.data.remote

import com.example.gouni_mobile_application.data.models.NewUser
import com.example.gouni_mobile_application.data.models.UserCredentials
import com.example.gouni_mobile_application.data.models.UserDTO

class AuthService {

    private val users = mutableListOf<NewUser>()

    fun login(credentials: UserCredentials): Result<UserDTO> {
        return if (credentials.email == "demo@demo.com" && credentials.password == "1234") {
            Result.success(
                UserDTO(
                    id = "1",
                    name = "Usuario Demo",
                    email = "demo@demo.com",
                    token = "fake-token"
                )
            )
        } else {
            Result.failure(Exception("Correo o contraseña incorrectos"))
        }

        // Verifica en usuarios registrados
        val user = users.find { it.email == credentials.email && it.password == credentials.password }
        return if (user != null) {
            Result.success(
                UserDTO(
                    id = user.email.hashCode().toString(),
                    name = user.userName,
                    email = user.email,
                    token = generateFakeToken(user.email)
                )
            )
        } else {
            Result.failure(Exception("Correo o contraseña incorrectos"))
        }
    }


    fun register(newUser: NewUser): Boolean {
        if (users.any { it.email == newUser.email }) {
            println("Ya existe un usuario con ese email.")
            return false
        }

        users.add(newUser)
        println("Usuario registrado exitosamente.")
        return true
    }

    private fun generateFakeToken(email: String): String {
        return "token_${email.hashCode()}_${System.currentTimeMillis()}"
    }
}