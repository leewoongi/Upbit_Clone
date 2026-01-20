package com.woon.domain.candle.usecase

import com.woon.domain.candle.entity.Candle
import com.woon.domain.candle.entity.constant.CandleType
import kotlinx.coroutines.flow.Flow

interface ObserveRealtimeCandlesUseCase {
    operator fun invoke(
        marketCode: String,
        candleType: CandleType = CandleType.MINUTE_1
    ): Flow<Candle>
}
