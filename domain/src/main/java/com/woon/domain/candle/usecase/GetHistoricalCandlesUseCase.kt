package com.woon.domain.candle.usecase

import com.woon.domain.candle.entity.Candle
import com.woon.domain.candle.entity.constant.CandleType

interface GetHistoricalCandlesUseCase {
    suspend operator fun invoke(
        marketCode: String,
        candleType: CandleType = CandleType.MINUTE_5,
        count: Int = 200,
        to: String? = null
    ): List<Candle>
}
