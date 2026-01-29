package com.woon.datasource.remote.socket.candle.module

import com.woon.datasource.remote.socket.candle.RealtimeCandleDataSource
import com.woon.datasource.remote.socket.candle.RealtimeCandleDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RealtimeCandleDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindRealtimeCandleDataSource(
        impl: RealtimeCandleDataSourceImpl
    ): RealtimeCandleDataSource
}
