package com.woon.domain.candle.usecase

import com.woon.domain.candle.entity.Candle

interface GetCandleUseCase {
    suspend operator fun invoke(unit: Int) : List<Candle> // 초당
}