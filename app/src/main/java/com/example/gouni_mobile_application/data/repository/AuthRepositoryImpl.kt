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
            val userEntity = userDao.getUserByCredentials(email, password)
            if (userEntity != null) {
                currentUser = userEntity.toDomain()
                Result.success(currentUser!!)
            } else {
                Result.failure(Exception("Credenciales inválidas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(name: String, email: String, password: String, university: String, userCode: String): Result<User> {
        return try {
            val existingUser = userDao.getUserByEmail(email)
            if (existingUser != null) {
                Result.failure(Exception("El usuario ya existe"))
            } else {
                val user = User(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    email = email,
                    university = university,
                    userCode = userCode
                )
                userDao.insertUser(user.toEntity(password))
                currentUser = user
                Result.success(user)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUser(user: User, password: String): Result<User> {
        return try {
            userDao.updateUser(user.toEntity(password))
            currentUser = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): User? = currentUser

    override suspend fun logout() {
        currentUser = null
    }
}