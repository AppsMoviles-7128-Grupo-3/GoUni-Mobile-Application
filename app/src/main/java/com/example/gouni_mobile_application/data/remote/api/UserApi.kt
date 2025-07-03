package com.example.gouni_mobile_application.data.remote.api

import com.example.gouni_mobile_application.data.remote.dto.UserDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @POST("api/users/register")
    suspend fun register(@Body user: UserDto, @Query("password") password: String): Response<UserDto>

    @POST("api/users/login")
    suspend fun login(@Query("email") email: String, @Query("password") password: String): Response<UserDto>

    @GET("api/users/forgot-password")
    suspend fun forgotPassword(@Query("email") email: String): Response<ResponseBody>

    @POST("api/users/reset-password")
    suspend fun resetPassword(@Query("email") email: String, @Query("newPassword") newPassword: String): Response<ResponseBody>

    @PUT("api/users/{id}")
    suspend fun edit(@Path("id") id: Long, @Body user: UserDto, @Query("password") password: String? = null): Response<UserDto>

    @GET("api/users/{id}")
    suspend fun getById(@Path("id") id: Long): Response<UserDto>
} 