package com.woon.datasource.remote.http.candle.module

import com.woon.datasource.remote.http.candle.RemoteCandleDataSource
import com.woon.datasource.remote.http.candle.RemoteCandleDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CandleDataSourceModule {
    @Binds
    abstract fun bindCandleDataSource(
        remoteCandleDataSourceImpl: RemoteCandleDataSourceImpl
    ): RemoteCandleDataSource

}