package com.woon.datasource.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.woon.datasource.local.room.entity.PendingErrorEventEntity

@Dao
interface PendingErrorEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: PendingErrorEventEntity): Long

    @Query("SELECT * FROM pending_error_events ORDER BY createdAt ASC")
    suspend fun getAll(): List<PendingErrorEventEntity>

    @Query("SELECT * FROM pending_error_events ORDER BY createdAt ASC LIMIT :limit")
    suspend fun getOldest(limit: Int): List<PendingErrorEventEntity>

    @Query("DELETE FROM pending_error_events WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM pending_error_events")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM pending_error_events")
    suspend fun count(): Int
}
