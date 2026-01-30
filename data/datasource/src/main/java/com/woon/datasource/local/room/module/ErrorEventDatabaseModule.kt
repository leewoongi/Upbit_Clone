package com.woon.datasource.local.room.module

import android.content.Context
import androidx.room.Room
import com.woon.datasource.local.room.dao.PendingErrorEventDao
import com.woon.datasource.local.room.database.ErrorEventDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ErrorEventDatabaseModule {

    @Provides
    @Singleton
    fun provideErrorEventDatabase(
        @ApplicationContext context: Context
    ): ErrorEventDatabase {
        return Room.databaseBuilder(
            context,
            ErrorEventDatabase::class.java,
            "error_event_database"
        ).fallbackToDestructiveMigration(true)
         .build()
    }

    @Provides
    @Singleton
    fun providePendingErrorEventDao(
        database: ErrorEventDatabase
    ): PendingErrorEventDao {
        return database.pendingErrorEventDao()
    }
}
