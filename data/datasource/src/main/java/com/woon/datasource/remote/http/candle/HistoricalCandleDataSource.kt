package com.woon.datasource.remote.http.candle

import com.woon.datasource.remote.http.candle.response.CandleResponse

interface HistoricalCandleDataSource {

    suspend fun getSecondCandles(
        marketCode: String,
        count: Int = 200,
        to: String? = null
    ): List<CandleResponse>

    suspend fun getMinuteCandles(
        marketCode: String,
        unit: Int = 1,
        count: Int = 200,
        to: String? = null
    ): List<CandleResponse>

    suspend fun getDayCandles(
        marketCode: String,
        count: Int = 200,
        to: String? = null
    ): List<CandleResponse>

    suspend fun getWeekCandles(
        marketCode: String,
        count: Int = 200,
        to: String? = null
    ): List<CandleResponse>

    suspend fun getMonthCandles(
        marketCode: String,
        count: Int = 200,
        to: String? = null
    ): List<CandleResponse>
}
