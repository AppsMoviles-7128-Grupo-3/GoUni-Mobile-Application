package com.example.gouni_mobile_application.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gouni_mobile_application.data.local.dao.ReservationDao
import com.example.gouni_mobile_application.data.local.dao.RouteDao
import com.example.gouni_mobile_application.data.local.dao.UserDao
import com.example.gouni_mobile_application.data.local.entity.ReservationEntity
import com.example.gouni_mobile_application.data.local.entity.RouteEntity
import com.example.gouni_mobile_application.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, RouteEntity::class, ReservationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun routeDao(): RouteDao
    abstract fun reservationDao(): ReservationDao
}