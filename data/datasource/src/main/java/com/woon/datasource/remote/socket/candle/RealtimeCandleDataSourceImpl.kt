package com.woon.datasource.remote.socket.candle

import android.util.Log
import com.google.gson.Gson
import com.woon.datasource.remote.socket.candle.response.WebSocketCandleResponse
import com.woon.network.websocket.WebSocketClient
import com.woon.network.websocket.request.WebSocketRequest
import com.woon.network.websocket.response.WebSocketResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RealtimeCandleDataSourceImpl @Inject constructor(
    private val webSocketClient: WebSocketClient,
    private val gson: Gson
) : RealtimeCandleDataSource {

    override fun observeCandles(
        marketCode: String,
        candleType: String
    ): Flow<WebSocketCandleResponse> {
        val request = WebSocketRequest.Builder()
            .url(CANDLE_URL)
            .codes(listOf(marketCode))
            .type(candleType)
            .build()

        return webSocketClient.observe(request).map { response ->
            when (response) {
                is WebSocketResponse.Success -> {
                    val json = response.toUtf8()
                    Log.d(TAG, "Candle JSON: $json")
                    gson.fromJson(json, WebSocketCandleResponse::class.java)
                }
                is WebSocketResponse.Failure -> {
                    throw response.throwable
                }
            }
        }
    }

    companion object {
        private const val TAG = "RealtimeCandleDataSource"
        private const val CANDLE_URL = "wss://api.upbit.com/websocket/v1"
    }
}
