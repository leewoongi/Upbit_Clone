package com.woon.domain.candle.repository

import com.woon.domain.candle.entity.Candle
import com.woon.domain.candle.entity.constant.CandleType
import kotlinx.coroutines.flow.Flow

interface CandleRepository {

    /**
     * 과거 캔들 데이터 조회 (REST API)
     */
    suspend fun getHistoricalCandles(
        marketCode: String,
        candleType: CandleType,
        count: Int = 200
    ): List<Candle>

    /**
     * 실시간 캔들 데이터 구독 (WebSocket)
     */
    fun observeRealtimeCandles(
        marketCode: String,
        candleType: CandleType
    ): Flow<Candle>
}
