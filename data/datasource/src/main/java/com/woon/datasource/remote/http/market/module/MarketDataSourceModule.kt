package com.woon.datasource.remote.http.market.module

import com.woon.datasource.remote.http.market.MarketDataSource
import com.woon.datasource.remote.http.market.MarketDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MarketDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindMarketDataSource(
        impl: MarketDataSourceImpl
    ): MarketDataSource
}
