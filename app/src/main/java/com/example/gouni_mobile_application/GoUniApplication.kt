package com.example.gouni_mobile_application

import android.app.Application
import com.example.gouni_mobile_application.domain.repository.AuthRepository
import com.example.gouni_mobile_application.domain.repository.CarRepository
import com.example.gouni_mobile_application.domain.repository.ReservationRepository
import com.example.gouni_mobile_application.domain.repository.RouteRepository
import com.example.gouni_mobile_application.data.di.DataModule

class GoUniApplication : Application() {

    val authRepository: AuthRepository by lazy {
        DataModule.getAuthRepository()
    }

    val routeRepository: RouteRepository by lazy {
        DataModule.getRouteRepository()
    }

    val reservationRepository: ReservationRepository by lazy {
        DataModule.getReservationRepository()
    }

    val carRepository: CarRepository by lazy {
        DataModule.getCarRepository()
    }
}