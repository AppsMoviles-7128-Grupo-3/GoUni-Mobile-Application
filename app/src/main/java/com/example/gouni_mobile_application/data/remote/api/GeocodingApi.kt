package com.example.gouni_mobile_application.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

// Data classes for Geocoding API response

data class GeocodingResponse(
    val results: List<GeocodingResult>,
    val status: String
)

data class GeocodingResult(
    val geometry: Geometry
)

data class Geometry(
    val location: Location
)

data class Location(
    val lat: Double,
    val lng: Double
)

interface GeocodingApi {
    @GET("geocode/json")
    suspend fun geocode(
        @Query("address") address: String,
        @Query("key") apiKey: String
    ): Response<GeocodingResponse>
} 