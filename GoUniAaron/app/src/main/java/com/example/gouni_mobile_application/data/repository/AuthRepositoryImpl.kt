package com.example.gouni_mobile_application.data.repository

import com.example.gouni_mobile_application.data.local.dao.UserDao
import com.example.gouni_mobile_application.data.mapper.toDomain
import com.example.gouni_mobile_application.data.mapper.toEntity
import com.example.gouni_mobile_application.domain.model.User
import com.example.gouni_mobile_application.domain.repository.AuthRepository
import java.util.UUID

class AuthRepositoryImpl(
    private val userDao: UserDao
) : AuthRepository {

    private var currentUser: User? = null

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            println("Intentando login con email: $email, password: $password")
            val userEntity = userDao.getUserByCredentials(email, password)
            println("Usuario encontrado: $userEntity")

            if (userEntity != null) {
                currentUser = userEntity.toDomain()
                println("Login exitoso para usuario: ${currentUser?.name}")
                Result.success(currentUser!!)
            } else {
                println("Credenciales inválidas")
                Result.failure(Exception("Credenciales inválidas"))
            }
        } catch (e: Exception) {
            println("Error en login: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun register(name: String, email: String, password: String, university: String): Result<User> {
        return try {
            println("Intentando registro con email: $email")
            val existingUser = userDao.getUserByEmail(email)

            if (existingUser != null) {
                println("Usuario ya existe")
                Result.failure(Exception("El usuario ya existe"))
            } else {
                val user = User(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    email = email,
                    university = university
                )
                userDao.insertUser(user.toEntity(password))
                currentUser = user
                println("Registro exitoso para usuario: ${user.name}")
                Result.success(user)
            }
        } catch (e: Exception) {
            println("Error en registro: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): User? = currentUser

    override suspend fun logout() {
        currentUser = null
    }
}