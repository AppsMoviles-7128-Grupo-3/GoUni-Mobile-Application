package com.example.gouni_mobile_application.data.repository

import com.example.gouni_mobile_application.data.remote.api.GeocodingApi
import com.example.gouni_mobile_application.data.remote.api.DirectionsApi
import com.example.gouni_mobile_application.data.remote.api.Location
import com.example.gouni_mobile_application.data.remote.api.DirectionsResponse
import com.example.gouni_mobile_application.data.di.DataModule
import com.example.gouni_mobile_application.BuildConfig
import retrofit2.Response
import com.example.gouni_mobile_application.domain.repository.MapRepository

class MapRepositoryImpl(
    private val geocodingApi: GeocodingApi = DataModule.geocodingApi,
    private val directionsApi: DirectionsApi = DataModule.directionsApi
) : MapRepository {
    override suspend fun getCoordinates(place: String): Location? {
        val response = geocodingApi.geocode(place, BuildConfig.GOOGLE_MAPS_API_KEY)
        return if (response.isSuccessful) {
            response.body()?.results?.firstOrNull()?.geometry?.location
        } else null
    }

    override suspend fun getRoutePolyline(origin: Location, destination: Location): String? {
        val originStr = "${origin.lat},${origin.lng}"
        val destStr = "${destination.lat},${destination.lng}"
        val response: Response<DirectionsResponse> = directionsApi.getDirections(
            originStr, destStr, BuildConfig.GOOGLE_MAPS_API_KEY
        )
        return if (response.isSuccessful) {
            response.body()?.routes?.firstOrNull()?.overview_polyline?.points
        } else null
    }
} 