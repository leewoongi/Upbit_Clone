package com.woon.network.websocket.module

import com.woon.network.websocket.WebSocketClient
import com.woon.network.websocket.WebSocketClientImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WebSocketModule {

    @Binds
    @Singleton
    abstract fun bindWebSocketClient(
        impl: WebSocketClientImpl
    ): WebSocketClient
}
