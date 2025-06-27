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

class GoUniApplication : Application() {

    val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "gouni_database"
        ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5).build()
    }

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(database.userDao())
    }

    val routeRepository: RouteRepository by lazy {
        RouteRepositoryImpl(database.routeDao())
    }

    val reservationRepository: ReservationRepository by lazy {
        ReservationRepositoryImpl()
    }

    val carRepository: CarRepository by lazy {
        CarRepositoryImpl(database.carDao())
    }

    override fun onCreate() {
        super.onCreate()
        insertTestData()
    }

    private fun insertTestData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.userDao().insertUser(
                    UserEntity(
                        id = "user1",
                        name = "Juan Pérez",
                        email = "test@example.com",
                        password = "123456",
                        university = "UPC",
                        userCode = "U20201234"
                    )
                )

                database.userDao().insertUser(
                    UserEntity(
                        id = "user2",
                        name = "María García",
                        email = "maria@example.com",
                        password = "password",
                        university = "UPC",
                        userCode = "U20195678"
                    )
                )

                println("Usuarios de prueba insertados")
            } catch (e: Exception) {
                println("Error o usuarios ya existen: ${e.message}")
            }
        }
    }
}