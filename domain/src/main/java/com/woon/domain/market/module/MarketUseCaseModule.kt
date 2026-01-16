package com.woon.domain.market.module

import com.woon.domain.market.service.GetMarketsService
import com.woon.domain.market.usecase.GetMarketsUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MarketUseCaseModule {

    @Binds
    @Singleton
    abstract fun bindGetMarketsUseCase(
        service: GetMarketsService
    ): GetMarketsUseCase
}
