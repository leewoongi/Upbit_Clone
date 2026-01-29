package com.woon.network.websocket

import com.woon.network.websocket.request.WebSocketRequest
import com.woon.network.websocket.response.WebSocketResponse
import kotlinx.coroutines.flow.Flow

interface WebSocketClient {
    fun observe(request: WebSocketRequest): Flow<WebSocketResponse>
}