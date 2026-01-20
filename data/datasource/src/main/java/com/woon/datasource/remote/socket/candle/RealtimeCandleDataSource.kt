package com.woon.datasource.remote.socket.candle

import com.woon.datasource.remote.socket.candle.response.WebSocketCandleResponse
import kotlinx.coroutines.flow.Flow

interface RealtimeCandleDataSource {
    fun observeCandles(
        marketCode: String,
        candleType: String = "candle.1m"
    ): Flow<WebSocketCandleResponse>
}
