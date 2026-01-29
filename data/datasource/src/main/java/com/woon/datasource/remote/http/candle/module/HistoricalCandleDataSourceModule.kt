package com.woon.datasource.remote.http.candle.module

import com.woon.datasource.remote.http.candle.HistoricalCandleDataSource
import com.woon.datasource.remote.http.candle.HistoricalCandleDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HistoricalCandleDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindHistoricalCandleDataSource(
        impl: HistoricalCandleDataSourceImpl
    ): HistoricalCandleDataSource
}
