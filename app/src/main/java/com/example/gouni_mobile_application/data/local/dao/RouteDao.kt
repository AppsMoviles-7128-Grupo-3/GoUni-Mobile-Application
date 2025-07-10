package com.example.gouni_mobile_application.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gouni_mobile_application.data.local.entity.RouteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RouteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoute(route: RouteEntity)

    @Query("SELECT * FROM routes WHERE userId = :userId")
    fun getRoutesByUser(userId: String): Flow<List<RouteEntity>>

    @Query("DELETE FROM routes WHERE id = :routeId")
    suspend fun deleteRoute(routeId: String)
}