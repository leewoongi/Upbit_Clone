package com.woon.domain.candle.module

import com.woon.domain.candle.service.GetHistoricalCandlesService
import com.woon.domain.candle.service.ObserveRealtimeCandlesService
import com.woon.domain.candle.usecase.GetHistoricalCandlesUseCase
import com.woon.domain.candle.usecase.ObserveRealtimeCandlesUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CandleUseCaseModule {

    @Binds
    abstract fun bindGetHistoricalCandlesUseCase(
        service: GetHistoricalCandlesService
    ): GetHistoricalCandlesUseCase

    @Binds
    abstract fun bindObserveRealtimeCandlesUseCase(
        service: ObserveRealtimeCandlesService
    ): ObserveRealtimeCandlesUseCase
}
