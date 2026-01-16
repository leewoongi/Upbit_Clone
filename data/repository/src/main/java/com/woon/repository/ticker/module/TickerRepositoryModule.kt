package com.woon.repository.ticker.module

import com.woon.domain.ticker.repository.TickerRepository
import com.woon.repository.ticker.TickerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TickerRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTickerRepository(
        impl: TickerRepositoryImpl
    ): TickerRepository
}
