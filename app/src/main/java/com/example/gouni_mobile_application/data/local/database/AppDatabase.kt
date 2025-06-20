package com.example.gouni_mobile_application.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.gouni_mobile_application.data.local.dao.CarDao
import com.example.gouni_mobile_application.data.local.dao.ReservationDao
import com.example.gouni_mobile_application.data.local.dao.RouteDao
import com.example.gouni_mobile_application.data.local.dao.UserDao
import com.example.gouni_mobile_application.data.local.entity.CarEntity
import com.example.gouni_mobile_application.data.local.entity.ReservationEntity
import com.example.gouni_mobile_application.data.local.entity.RouteEntity
import com.example.gouni_mobile_application.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, RouteEntity::class, ReservationEntity::class, CarEntity::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun routeDao(): RouteDao
    abstract fun reservationDao(): ReservationDao
    abstract fun carDao(): CarDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE users ADD COLUMN userCode TEXT NOT NULL DEFAULT ''")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {

        database.execSQL("""
            CREATE TABLE cars (
                id TEXT PRIMARY KEY NOT NULL,
                userId TEXT NOT NULL,
                make TEXT NOT NULL,
                model TEXT NOT NULL,
                licensePlate TEXT NOT NULL,
                color TEXT NOT NULL,
                year INTEGER NOT NULL,
                insuranceInfo TEXT NOT NULL,
                registrationNumber TEXT NOT NULL
            )
        """)

        database.execSQL("ALTER TABLE routes ADD COLUMN carId TEXT NOT NULL DEFAULT ''")
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {

        database.execSQL("ALTER TABLE cars ADD COLUMN insuranceBrand TEXT NOT NULL DEFAULT ''")
    }
}