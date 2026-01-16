package com.woon.domain.ticker.module

import com.woon.domain.ticker.service.GetTickersService
import com.woon.domain.ticker.usecase.GetTickersUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TickerUseCaseModule {

    @Binds
    @Singleton
    abstract fun bindGetTickersUseCase(
        service: GetTickersService
    ): GetTickersUseCase
}
