package com.woon.network.websocket

import com.woon.network.websocket.request.WebSocketRequest
import com.woon.network.websocket.response.WebSocketResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import javax.inject.Inject

class WebSocketClientImpl @Inject constructor(
    private val okHttpClient: OkHttpClient
) : WebSocketClient,WebSocketListener()  {

    override fun observe(
        request: WebSocketRequest
    ): Flow<WebSocketResponse> = callbackFlow {
        val httpRequest = Request.Builder()
            .url(request.url)
            .build()

        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                // 구독 메시지 전송
                webSocket.send(request.toSubscribeMessage())
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                trySend(WebSocketResponse.Success(text))
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                trySend(WebSocketResponse.Success(bytes))
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                // 서버가 닫으려는 중 → 클라이언트도 닫기
                webSocket.close(code, reason)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                // 소켓이 닫히면 flow도 종료
                close()
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                // 에러 이벤트 전달 후 flow 종료
                trySend(WebSocketResponse.Failure(t))
                close(t)
            }
        }

        // ✅ collect 시작 시점에 소켓 연결
        val webSocket = okHttpClient.newWebSocket(httpRequest, listener)

        // ✅ collect 취소/종료 시점에 소켓 종료
        awaitClose {
            // 이미 닫힌 경우도 안전
            webSocket.close(1000, "Collector cancelled")
        }
    }
}
