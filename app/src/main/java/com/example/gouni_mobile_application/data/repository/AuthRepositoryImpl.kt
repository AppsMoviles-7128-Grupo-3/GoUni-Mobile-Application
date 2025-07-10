package com.example.gouni_mobile_application.data.repository

import com.example.gouni_mobile_application.data.remote.api.UserApi
import com.example.gouni_mobile_application.data.remote.dto.UserDto
import com.example.gouni_mobile_application.domain.model.User
import com.example.gouni_mobile_application.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val userApi: UserApi
) : AuthRepository {

    private var currentUser: User? = null

    override suspend fun login(email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        val response = userApi.login(email, password)
        if (response.isSuccessful) {
            response.body()?.let { dto ->
                return@withContext Result.success(dto.toDomain())
            }
            // If response is successful but body is null, it means wrong credentials
            return@withContext Result.failure(Exception("Email o contraseña incorrecta"))
        } else if (response.code() == 401) {
            // HTTP 401 Unauthorized - wrong credentials
            return@withContext Result.failure(Exception("Email o contraseña incorrecta"))
        } else {
            // For other HTTP errors, return a generic error message
            Result.failure(Exception("Email o contraseña incorrecta"))
        }
    }

    override suspend fun register(name: String, email: String, password: String, university: String, userCode: String): Result<User> = withContext(Dispatchers.IO) {
        val dto = UserDto(null, name, email, university, userCode)
        val response = userApi.register(dto, password)
        if (response.isSuccessful) {
            response.body()?.let { dto ->
                return@withContext Result.success(dto.toDomain())
            }
        }
        Result.failure(Exception(response.message()))
    }

    override suspend fun updateUser(user: User, password: String): Result<User> = withContext(Dispatchers.IO) {
        val dto = user.toDto()
        val passwordParam = if (password.isNotBlank()) password else null
        val response = userApi.edit(user.id?.toLong() ?: -1, dto, passwordParam)
        if (response.isSuccessful) {
            response.body()?.let { dto ->
                return@withContext Result.success(dto.toDomain())
            }
        }
        Result.failure(Exception(response.message()))
    }

    override suspend fun getCurrentUser(): User? = currentUser

    override suspend fun logout() {
        currentUser = null
    }

    override suspend fun getUserById(userId: String): User? = withContext(Dispatchers.IO) {
        val response = userApi.getById(userId.toLong())
        if (response.isSuccessful) {
            response.body()?.toDomain()
        } else {
            null
        }
    }

    override fun getUserByIdFlow(userId: String): Flow<User?> = kotlinx.coroutines.flow.flow {
        val response = userApi.getById(userId.toLong())
        if (response.isSuccessful) {
            emit(response.body()?.toDomain())
        } else {
            emit(null)
        }
    }

    override suspend fun emailExists(email: String): Boolean = withContext(Dispatchers.IO) {
        val response = userApi.forgotPassword(email)
        if (response.isSuccessful) {
            val responseBody = response.body()
            val message = responseBody?.string() ?: ""
            message.contains("Correo enviado a")
        } else {
            false
        }
    }

    override suspend fun updatePasswordByEmail(email: String, newPassword: String): Result<Unit> = withContext(Dispatchers.IO) {
        val response = userApi.resetPassword(email, newPassword)
        if (response.isSuccessful) {
            val responseBody = response.body()
            val message = responseBody?.string() ?: ""
            if (message.contains("Contraseña actualizada correctamente")) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(message.ifEmpty { "Unknown error" }))
            }
        } else {
            val errorMessage = response.body()?.string() ?: response.message()
            Result.failure(Exception(errorMessage))
        }
    }
}

// Extension functions for mapping
private fun UserDto.toDomain() = User(
    id = id?.toString() ?: "",
    name = name,
    email = email,
    university = university,
    userCode = userCode
)

private fun User.toDto() = UserDto(
    id = id?.toLongOrNull(),
    name = name,
    email = email,
    university = university,
    userCode = userCode
)