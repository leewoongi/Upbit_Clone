package com.woon.domain.trade.module

import com.woon.domain.trade.service.ObserveTradesService
import com.woon.domain.trade.usecase.ObserveTradesUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TradeUseCaseModule {

    @Binds
    @Singleton
    abstract fun bindObserveTradesUseCase(
        service: ObserveTradesService
    ): ObserveTradesUseCase
}
