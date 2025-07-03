package com.example.gouni_mobile_application.data.di

import android.content.Context
import androidx.room.Room
import com.example.gouni_mobile_application.data.local.database.AppDatabase
import com.example.gouni_mobile_application.data.repository.AuthRepositoryImpl
import com.example.gouni_mobile_application.data.repository.CarRepositoryImpl
import com.example.gouni_mobile_application.data.repository.ReservationRepositoryImpl
import com.example.gouni_mobile_application.data.repository.RouteRepositoryImpl
import com.example.gouni_mobile_application.domain.repository.AuthRepository
import com.example.gouni_mobile_application.domain.repository.CarRepository
import com.example.gouni_mobile_application.domain.repository.ReservationRepository
import com.example.gouni_mobile_application.domain.repository.RouteRepository
import com.example.gouni_mobile_application.data.remote.api.UserApi
import com.example.gouni_mobile_application.data.remote.api.CarApi
import com.example.gouni_mobile_application.data.remote.api.RouteApi
import com.example.gouni_mobile_application.data.remote.api.ReservationApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataModule {
    private const val BASE_URL = "http://10.0.2.2:8080/api/" // Change as needed

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val userApi: UserApi by lazy { retrofit.create(UserApi::class.java) }
    val carApi: CarApi by lazy { retrofit.create(CarApi::class.java) }
    val routeApi: RouteApi by lazy { retrofit.create(RouteApi::class.java) }
    val reservationApi: ReservationApi by lazy { retrofit.create(ReservationApi::class.java) }

    fun getAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "gouni_database"
        )
            .fallbackToDestructiveMigration(false)
        .build()
    }

    fun getAuthRepository(): AuthRepository {
        return AuthRepositoryImpl(userApi)
    }

    fun getRouteRepository(): RouteRepository {
        return RouteRepositoryImpl(routeApi)
    }

    fun getReservationRepository(): ReservationRepository {
        return ReservationRepositoryImpl(reservationApi)
    }

    fun getCarRepository(): CarRepository {
        return CarRepositoryImpl(carApi)
    }
} 