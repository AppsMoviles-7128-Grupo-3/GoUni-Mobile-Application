package com.example.gouni_mobile_application

import android.app.Application
import androidx.room.Room
import com.example.gouni_mobile_application.data.local.database.AppDatabase
import com.example.gouni_mobile_application.data.local.database.MIGRATION_1_2
import com.example.gouni_mobile_application.data.local.database.MIGRATION_2_3
import com.example.gouni_mobile_application.data.local.database.MIGRATION_3_4
import com.example.gouni_mobile_application.data.local.database.MIGRATION_4_5
import com.example.gouni_mobile_application.data.local.entity.UserEntity
import com.example.gouni_mobile_application.data.repository.AuthRepositoryImpl
import com.example.gouni_mobile_application.data.repository.CarRepositoryImpl
import com.example.gouni_mobile_application.data.repository.ReservationRepositoryImpl
import com.example.gouni_mobile_application.data.repository.RouteRepositoryImpl
import com.example.gouni_mobile_application.domain.repository.AuthRepository
import com.example.gouni_mobile_application.domain.repository.CarRepository
import com.example.gouni_mobile_application.domain.repository.ReservationRepository
import com.example.gouni_mobile_application.domain.repository.RouteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.gouni_mobile_application.data.di.DataModule

class GoUniApplication : Application() {

    // Remove database property if not needed for backend-managed entities
    // val database by lazy {
    //     DataModule.getAppDatabase(applicationContext)
    // }

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

    override fun onCreate() {
        super.onCreate()
        // Removed insertTestData and all database references
    }
}