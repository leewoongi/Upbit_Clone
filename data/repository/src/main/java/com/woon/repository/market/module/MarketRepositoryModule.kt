package com.woon.repository.market.module

import com.woon.domain.market.repository.MarketRepository
import com.woon.repository.market.MarketRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MarketRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMarketRepository(
        impl: MarketRepositoryImpl
    ): MarketRepository
}
