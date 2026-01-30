package com.woon.datasource.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.woon.datasource.local.room.dao.PendingErrorEventDao
import com.woon.datasource.local.room.entity.PendingErrorEventEntity

@Database(
    entities = [PendingErrorEventEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ErrorEventDatabase : RoomDatabase() {
    abstract fun pendingErrorEventDao(): PendingErrorEventDao
}
