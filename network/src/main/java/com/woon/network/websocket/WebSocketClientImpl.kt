package com.woon.network.websocket

import com.woon.network.websocket.request.WebSocketRequest
import com.woon.network.websocket.response.WebSocketResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import javax.inject.Inject

class WebSocketClientImpl
@Inject constructor(
    private val okHttpClient: OkHttpClient
) : WebSocketClient, WebSocketListener() {
    private var webSocket: WebSocket? = null
    private var websocketRequest: WebSocketRequest? = null
    private val _response: MutableSharedFlow<WebSocketResponse> = MutableSharedFlow(extraBufferCapacity = 64)
    val response = _response.asSharedFlow()


    override fun connect(request: WebSocketRequest) {
        websocketRequest = request
        val httpRequest = Request
            .Builder()
            .url(request.url)
            .build()

        webSocket = okHttpClient.newWebSocket(httpRequest, this)
    }

    override fun disconnect() {
        this.webSocket?.close(1000, "Client disconnect")  // 서버에 종료 알림!
        this.webSocket = null
        this.websocketRequest = null
    }

    override fun receive(): Flow<WebSocketResponse> {
        return response
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        if (websocketRequest == null) return
        webSocket.send(websocketRequest!!.toSubscribeMessage())
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        _response.tryEmit(WebSocketResponse.Success(text))
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        _response.tryEmit(WebSocketResponse.Success(bytes))
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(code, reason)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        this.webSocket = null
        this.websocketRequest = null
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        this.webSocket = null
        _response.tryEmit(WebSocketResponse.Failure(t))
    }
}
