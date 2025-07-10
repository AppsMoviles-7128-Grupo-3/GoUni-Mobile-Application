package com.example.gouni_mobile_application.data.remote.api

import com.example.gouni_mobile_application.data.remote.dto.RouteDto
import retrofit2.Response
import retrofit2.http.*

interface RouteApi {
    @POST("api/routes")
    suspend fun create(@Body route: RouteDto): Response<RouteDto>

    @PUT("api/routes/{id}")
    suspend fun update(@Path("id") id: Long, @Body route: RouteDto): Response<RouteDto>

    @DELETE("api/routes/{id}")
    suspend fun delete(@Path("id") id: Long): Response<Unit>

    @GET("api/routes/{id}")
    suspend fun getById(@Path("id") id: Long): Response<RouteDto>

    @GET("api/routes")
    suspend fun getAll(): Response<List<RouteDto>>

    @GET("api/routes/driver/{driverId}")
    suspend fun getByDriverId(@Path("driverId") driverId: Long): Response<List<RouteDto>>

    @GET("api/routes/user/{userId}")
    suspend fun getByUserId(@Path("userId") userId: Long): Response<List<RouteDto>>
} 