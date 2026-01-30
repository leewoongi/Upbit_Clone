package com.woon.datasource.local.preferences.module

import com.woon.datasource.local.preferences.LocalPreferencesDataSource
import com.woon.datasource.local.preferences.LocalPreferencesDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalPreferencesModule {

    @Binds
    @Singleton
    abstract fun bindLocalPreferencesDataSource(
        impl: LocalPreferencesDataSourceImpl
    ): LocalPreferencesDataSource
}
