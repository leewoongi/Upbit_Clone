package com.woon.datasource.remote.socket.ticker.module

import com.woon.datasource.remote.socket.ticker.TickerDataSource
import com.woon.datasource.remote.socket.ticker.TickerDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TickerDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindTickerDataSource(
        impl: TickerDataSourceImpl
    ): TickerDataSource
}