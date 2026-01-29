package com.woon.domain.event.module

import com.woon.domain.event.service.SendErrorEventService
import com.woon.domain.event.usecase.SendErrorEventUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ErrorEventUseCaseModule {

    @Binds
    @Singleton
    abstract fun bindSendErrorEventUseCase(
        service: SendErrorEventService
    ): SendErrorEventUseCase
}
