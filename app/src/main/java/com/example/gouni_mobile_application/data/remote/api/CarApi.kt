package com.example.gouni_mobile_application.data.remote.api

import com.example.gouni_mobile_application.data.remote.dto.CarDto
import retrofit2.Response
import retrofit2.http.*

interface CarApi {
    @POST("api/cars")
    suspend fun create(@Body car: CarDto): Response<CarDto>

    @PUT("api/cars/{id}")
    suspend fun update(@Path("id") id: Long, @Body car: CarDto): Response<CarDto>

    @DELETE("api/cars/{id}")
    suspend fun delete(@Path("id") id: Long): Response<Unit>

    @GET("api/cars/{id}")
    suspend fun getById(@Path("id") id: Long): Response<CarDto>

    @GET("api/cars/user/{userId}")
    suspend fun getByUserId(@Path("userId") userId: Long): Response<List<CarDto>>
} 