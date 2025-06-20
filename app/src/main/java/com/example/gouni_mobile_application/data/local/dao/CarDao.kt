package com.example.gouni_mobile_application.data.local.dao

import androidx.room.*
import com.example.gouni_mobile_application.data.local.entity.CarEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {
    @Query("SELECT * FROM cars WHERE userId = :userId")
    fun getCarByUserId(userId: String): Flow<CarEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCar(car: CarEntity)

    @Update
    suspend fun updateCar(car: CarEntity)

    @Delete
    suspend fun deleteCar(car: CarEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM cars WHERE userId = :userId)")
    suspend fun hasCar(userId: String): Boolean
} 