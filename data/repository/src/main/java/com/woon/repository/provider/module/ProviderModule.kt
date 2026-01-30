package com.woon.repository.provider.module

import com.woon.domain.event.provider.DeviceInfoProvider
import com.woon.domain.event.provider.InstallIdProvider
import com.woon.domain.event.provider.NetworkStateProvider
import com.woon.repository.event.InstallIdProviderImpl
import com.woon.repository.event.NetworkStateProviderImpl
import com.woon.repository.provider.DeviceInfoProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProviderModule {

    @Binds
    @Singleton
    abstract fun bindDeviceInfoProvider(
        impl: DeviceInfoProviderImpl
    ): DeviceInfoProvider

    @Binds
    @Singleton
    abstract fun bindNetworkStateProvider(
        impl: NetworkStateProviderImpl
    ): NetworkStateProvider

    @Binds
    @Singleton
    abstract fun bindInstallIdProvider(
        impl: InstallIdProviderImpl
    ): InstallIdProvider
}
