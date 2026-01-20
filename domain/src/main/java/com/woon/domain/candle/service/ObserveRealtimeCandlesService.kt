package com.woon.domain.candle.service

import com.woon.domain.candle.entity.Candle
import com.woon.domain.candle.entity.constant.CandleType
import com.woon.domain.candle.repository.CandleRepository
import com.woon.domain.candle.usecase.ObserveRealtimeCandlesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRealtimeCandlesService @Inject constructor(
    private val candleRepository: CandleRepository
) : ObserveRealtimeCandlesUseCase {

    override fun invoke(
        marketCode: String,
        candleType: CandleType
    ): Flow<Candle> {
        return candleRepository.observeRealtimeCandles(marketCode, candleType)
    }
}
