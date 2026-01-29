package com.woon.repository.breadcrumb.module

import com.woon.domain.breadcrumb.storage.BreadcrumbStorage
import com.woon.repository.breadcrumb.BreadcrumbStorageImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BreadcrumbStorageModule {

    @Binds
    @Singleton
    abstract fun bindBreadcrumbStorage(
        impl: BreadcrumbStorageImpl
    ): BreadcrumbStorage
}
