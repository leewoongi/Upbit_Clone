package com.woon.network.websocket

import okio.ByteString

sealed interface WebSocketEvent {
    data object Open : WebSocketEvent
    data class Text(val text: String) : WebSocketEvent
    data class Binary(val bytes: ByteString) : WebSocketEvent
    data class Error(val throwable: Throwable) : WebSocketEvent
}
