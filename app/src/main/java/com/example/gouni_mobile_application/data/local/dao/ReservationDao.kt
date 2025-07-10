package com.example.gouni_mobile_application.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gouni_mobile_application.data.local.entity.ReservationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationDao {
    @Insert
    suspend fun insertReservation(reservation: ReservationEntity)

    @Query("SELECT * FROM reservations WHERE routeId IN (SELECT id FROM routes WHERE userId = :userId)")
    fun getReservationsByUser(userId: String): Flow<List<ReservationEntity>>
}