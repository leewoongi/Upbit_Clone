package com.woon.network.websocket.response

import okio.ByteString

sealed class WebSocketResponse {
    data class Success(private val data: Any) : WebSocketResponse() {
        fun toUtf8(): String = when (data) {
            is String -> data
            is ByteString -> data.utf8()
            else -> throw IllegalArgumentException("Unsupported type: ${data::class}")
        }
    }
    data class Failure(val throwable: Throwable) : WebSocketResponse()
}