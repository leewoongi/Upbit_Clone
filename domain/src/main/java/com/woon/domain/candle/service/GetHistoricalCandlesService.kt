package com.woon.domain.candle.service

import com.woon.domain.candle.entity.Candle
import com.woon.domain.candle.entity.constant.CandleType
import com.woon.domain.candle.repository.CandleRepository
import com.woon.domain.candle.usecase.GetHistoricalCandlesUseCase
import javax.inject.Inject

class GetHistoricalCandlesService @Inject constructor(
    private val candleRepository: CandleRepository
) : GetHistoricalCandlesUseCase {

    override suspend fun invoke(
        marketCode: String,
        candleType: CandleType,
        count: Int,
        to: String?
    ): List<Candle> {
        return candleRepository.getHistoricalCandles(marketCode, candleType, count, to)
    }
}
