package com.example.gouni_mobile_application.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gouni_mobile_application.data.models.RouteEntity

@Dao
interface RoutesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(route: RouteEntity)

    @Query("SELECT * FROM routes")
    suspend fun getAllRoutes(): List<RouteEntity>
}