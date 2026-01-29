package com.woon.datasource.remote.http.candle

import com.woon.datasource.remote.http.candle.api.CandleApi
import com.woon.datasource.remote.http.candle.response.CandleResponse
import javax.inject.Inject

class HistoricalCandleDataSourceImpl @Inject constructor(
    private val candleApi: CandleApi
) : HistoricalCandleDataSource {

    override suspend fun getSecondCandles(
        marketCode: String,
        count: Int,
        to: String?
    ): List<CandleResponse> {
        return candleApi.getSecondCandles(
            market = marketCode,
            count = count,
            to = to
        )
    }

    override suspend fun getMinuteCandles(
        marketCode: String,
        unit: Int,
        count: Int,
        to: String?
    ): List<CandleResponse> {
        return candleApi.getMinuteCandles(
            unit = unit,
            market = marketCode,
            count = count,
            to = to
        )
    }

    override suspend fun getDayCandles(
        marketCode: String,
        count: Int,
        to: String?
    ): List<CandleResponse> {
        return candleApi.getDayCandles(
            market = marketCode,
            count = count,
            to = to
        )
    }

    override suspend fun getWeekCandles(
        marketCode: String,
        count: Int,
        to: String?
    ): List<CandleResponse> {
        return candleApi.getWeekCandles(
            market = marketCode,
            count = count,
            to = to
        )
    }

    override suspend fun getMonthCandles(
        marketCode: String,
        count: Int,
        to: String?
    ): List<CandleResponse> {
        return candleApi.getMonthCandles(
            market = marketCode,
            count = count,
            to = to
        )
    }
}
