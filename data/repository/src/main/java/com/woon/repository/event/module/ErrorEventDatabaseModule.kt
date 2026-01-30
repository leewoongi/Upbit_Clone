package com.woon.repository.event.module

import com.woon.domain.event.storage.PendingErrorEventStorage
import com.woon.repository.event.PendingErrorEventStorageImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ErrorEventStorageModule {

    @Binds
    @Singleton
    abstract fun bindPendingErrorEventStorage(
        impl: PendingErrorEventStorageImpl
    ): PendingErrorEventStorage
}
