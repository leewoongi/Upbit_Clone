package com.woon.datasource.remote.http.event.module

import com.woon.datasource.remote.http.event.ErrorEventDataSource
import com.woon.datasource.remote.http.event.ErrorEventDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ErrorEventDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindErrorEventDataSource(
        impl: ErrorEventDataSourceImpl
    ): ErrorEventDataSource
}
