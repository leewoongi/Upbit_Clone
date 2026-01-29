package com.woon.chart.provider.module

import com.woon.chart.provider.AppInfoProviderImpl
import com.woon.domain.event.provider.AppInfoProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppInfoProviderModule {

    @Binds
    @Singleton
    abstract fun bindAppInfoProvider(
        impl: AppInfoProviderImpl
    ): AppInfoProvider
}
