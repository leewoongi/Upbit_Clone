package com.woon.repository.trade.module

import com.woon.domain.trade.repository.TradeRepository
import com.woon.repository.trade.TradeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TradeRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTradeRepository(
        impl: TradeRepositoryImpl
    ): TradeRepository
}
