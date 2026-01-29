package com.woon.datasource.remote.socket.ticker

import android.util.Log
import com.google.gson.Gson
import com.woon.datasource.remote.socket.ticker.response.TickerResponse
import com.woon.domain.ticker.entity.Ticker
import com.woon.network.websocket.WebSocketClient
import com.woon.network.websocket.request.WebSocketRequest
import com.woon.network.websocket.response.WebSocketResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class TickerDataSourceImpl
@Inject constructor(
    private val webSocketClient: WebSocketClient,
    private val gson: Gson
) : TickerDataSource {

    override fun getTicker(
        marketList: List<String>
    ): Flow<Ticker> {
        val request = WebSocketRequest.Builder()
            .url(TICKER_URL)
            .codes(marketList)
            .build()


        return webSocketClient.observe(request).map { response ->
            when (response) {
                is WebSocketResponse.Success -> {
                    val json = response.toUtf8()
                    val ticker = gson.fromJson(json, TickerResponse::class.java).toDomain()
                    ticker
                }
                is WebSocketResponse.Failure -> {
                    throw response.throwable
                }
            }
        }
    }

    companion object {
        private const val TAG = "TickerDataSource"
        private const val TICKER_URL = "wss://api.upbit.com/websocket/v1"
    }
}
