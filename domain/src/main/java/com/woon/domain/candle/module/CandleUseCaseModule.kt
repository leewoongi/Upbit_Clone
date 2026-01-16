package com.woon.domain.candle.module

import com.woon.domain.candle.service.CalculatePriceRangeService
import com.woon.domain.candle.service.GetCandleService
import com.woon.domain.candle.usecase.CalculatePriceRangeUseCase
import com.woon.domain.candle.usecase.GetCandleUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CandleUseCaseModule {

    @Binds
    abstract fun bindGetCandleUseCase(
        getCandleService: GetCandleService
    ): GetCandleUseCase

    @Binds
    abstract fun bindCalculatePriceRangeUseCase(
        calculatePriceRangeService: CalculatePriceRangeService
    ): CalculatePriceRangeUseCase

}