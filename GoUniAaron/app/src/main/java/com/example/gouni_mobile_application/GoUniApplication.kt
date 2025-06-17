package com.example.gouni_mobile_application

import android.app.Application
import androidx.room.Room
import com.example.gouni_mobile_application.data.local.database.AppDatabase
import com.example.gouni_mobile_application.data.local.entity.UserEntity
import com.example.gouni_mobile_application.data.repository.AuthRepositoryImpl
import com.example.gouni_mobile_application.data.repository.ReservationRepositoryImpl
import com.example.gouni_mobile_application.data.repository.RouteRepositoryImpl
import com.example.gouni_mobile_application.domain.repository.AuthRepository
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
        ).build()
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

    override fun onCreate() {
        super.onCreate()
        // Insertar datos de prueba siempre
        insertTestData()
    }

    private fun insertTestData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Insertar usuarios de prueba directamente
                database.userDao().insertUser(
                    UserEntity(
                        id = "user1",
                        name = "Juan Pérez",
                        email = "test@example.com",
                        password = "123456",
                        university = "Universidad Nacional"
                    )
                )

                database.userDao().insertUser(
                    UserEntity(
                        id = "user2",
                        name = "María García",
                        email = "maria@example.com",
                        password = "password",
                        university = "Universidad Central"
                    )
                )

                println("Usuarios de prueba insertados")
            } catch (e: Exception) {
                println("Error o usuarios ya existen: ${e.message}")
            }
        }
    }
}