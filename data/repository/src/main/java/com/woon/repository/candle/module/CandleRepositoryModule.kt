package com.woon.repository.candle.module

import com.woon.domain.candle.repository.CandleRepository
import com.woon.repository.candle.GetCandleRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CandleRepositoryModule {

    @Binds
    abstract fun bindCandleRepository(
        getCandleRepositoryImpl: GetCandleRepositoryImpl
    ): CandleRepository

}