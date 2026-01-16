package com.woon.domain.candle.repository

import com.woon.domain.candle.entity.Candle

interface CandleRepository {
    suspend fun getCandle(unit: Int) : List<Candle>
}