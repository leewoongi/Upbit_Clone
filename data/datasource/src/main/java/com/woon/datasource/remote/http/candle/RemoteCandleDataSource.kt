package com.woon.datasource.remote.http.candle

import com.woon.datasource.remote.http.candle.response.CandleResponse

interface RemoteCandleDataSource {
    suspend fun getCandle(unit: Int) : List<CandleResponse>
}