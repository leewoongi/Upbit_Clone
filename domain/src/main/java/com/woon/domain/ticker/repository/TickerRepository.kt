package com.woon.domain.ticker.repository

import com.woon.domain.ticker.entity.Ticker
import kotlinx.coroutines.flow.Flow

interface TickerRepository {
    fun observeTickers(codes: List<String>): Flow<Ticker>
}
