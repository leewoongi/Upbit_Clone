package com.woon.domain.candle.service

import com.woon.domain.candle.entity.Candle
import com.woon.domain.candle.repository.CandleRepository
import com.woon.domain.candle.usecase.GetCandleUseCase
import javax.inject.Inject

class GetCandleService
@Inject constructor(
    private val candleRepository: CandleRepository
) : GetCandleUseCase {

    override suspend fun invoke(unit: Int): List<Candle> {
        return candleRepository.getCandle(unit)
    }
}