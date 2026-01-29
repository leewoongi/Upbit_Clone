package com.woon.domain.candle.repository

import com.woon.domain.candle.entity.Candle
import com.woon.domain.candle.entity.constant.CandleType
import kotlinx.coroutines.flow.Flow

interface CandleRepository {

    /**
     * 과거 캔들 데이터 조회 (REST API)
     *
     * @param to 마지막 캔들 시각 (yyyy-MM-dd'T'HH:mm:ss), null이면 최신 데이터부터 조회
     */
    suspend fun getHistoricalCandles(
        marketCode: String,
        candleType: CandleType,
        count: Int = 200,
        to: String? = null
    ): List<Candle>

    /**
     * 실시간 캔들 데이터 구독 (WebSocket)
     */
    fun observeRealtimeCandles(
        marketCode: String,
        candleType: CandleType
    ): Flow<Candle>
}
