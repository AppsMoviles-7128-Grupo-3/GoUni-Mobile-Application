package com.example.gouni_mobile_application.data.remote.api

import com.example.gouni_mobile_application.data.remote.dto.ReservationDto
import retrofit2.Response
import retrofit2.http.*

interface ReservationApi {
    @POST("api/reservations")
    suspend fun create(@Body reservation: ReservationDto): Response<ReservationDto>

    @GET("api/reservations/route/{routeId}")
    suspend fun getByRouteId(@Path("routeId") routeId: Long): Response<List<ReservationDto>>

    @GET("api/reservations/passenger/{passengerId}")
    suspend fun getByPassengerId(@Path("passengerId") passengerId: Long): Response<List<ReservationDto>>

    @GET("api/reservations/driver/{driverId}")
    suspend fun getByDriverId(@Path("driverId") driverId: Long): Response<List<ReservationDto>>
} 