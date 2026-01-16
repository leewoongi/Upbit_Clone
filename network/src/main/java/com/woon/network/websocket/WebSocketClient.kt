package com.woon.network.websocket

import com.woon.network.websocket.request.WebSocketRequest
import com.woon.network.websocket.response.WebSocketResponse
import kotlinx.coroutines.flow.Flow

interface WebSocketClient {
    fun connect(request: WebSocketRequest)
    fun disconnect()

    fun receive() : Flow<WebSocketResponse>
}