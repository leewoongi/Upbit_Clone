package com.woon.repository.candle

import com.woon.datasource.remote.http.candle.HistoricalCandleDataSource
import com.woon.datasource.remote.socket.candle.RealtimeCandleDataSource
import com.woon.domain.candle.entity.Candle
import com.woon.domain.candle.entity.constant.CandleType
import com.woon.domain.candle.repository.CandleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CandleRepositoryImpl @Inject constructor(
    private val historicalCandleDataSource: HistoricalCandleDataSource,
    private val realtimeCandleDataSource: RealtimeCandleDataSource
) : CandleRepository {

    override suspend fun getHistoricalCandles(
        marketCode: String,
        candleType: CandleType,
        count: Int,
        to: String?
    ): List<Candle> {
        val responses = when (candleType) {
            CandleType.SECOND -> historicalCandleDataSource.getSecondCandles(marketCode, count, to)
            CandleType.MINUTE_1 -> historicalCandleDataSource.getMinuteCandles(marketCode, 1, count, to)
            CandleType.MINUTE_3 -> historicalCandleDataSource.getMinuteCandles(marketCode, 3, count, to)
            CandleType.MINUTE_5 -> historicalCandleDataSource.getMinuteCandles(marketCode, 5, count, to)
            CandleType.MINUTE_10 -> historicalCandleDataSource.getMinuteCandles(marketCode, 10, count, to)
            CandleType.MINUTE_15 -> historicalCandleDataSource.getMinuteCandles(marketCode, 15, count, to)
            CandleType.MINUTE_30 -> historicalCandleDataSource.getMinuteCandles(marketCode, 30, count, to)
            CandleType.MINUTE_60 -> historicalCandleDataSource.getMinuteCandles(marketCode, 60, count, to)
            CandleType.MINUTE_240 -> historicalCandleDataSource.getMinuteCandles(marketCode, 240, count, to)
            CandleType.DAY -> historicalCandleDataSource.getDayCandles(marketCode, count, to)
            CandleType.WEEK -> historicalCandleDataSource.getWeekCandles(marketCode, count, to)
            CandleType.MONTH -> historicalCandleDataSource.getMonthCandles(marketCode, count, to)
        }
        return responses.map { it.toDomain(candleType) }
    }

    override fun observeRealtimeCandles(
        marketCode: String,
        candleType: CandleType
    ): Flow<Candle> {
        return realtimeCandleDataSource.observeCandles(marketCode, candleType.wsType)
            .map { it.toDomain() }
    }
}
