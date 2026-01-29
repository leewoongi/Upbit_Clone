package com.woon.repository.event.module

import com.woon.domain.event.repository.ErrorEventRepository
import com.woon.repository.event.ErrorEventRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ErrorEventRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindErrorEventRepository(
        impl: ErrorEventRepositoryImpl
    ): ErrorEventRepository
}
