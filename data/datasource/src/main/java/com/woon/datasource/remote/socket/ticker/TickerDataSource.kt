package com.woon.datasource.remote.socket.ticker

import com.woon.domain.ticker.entity.Ticker
import kotlinx.coroutines.flow.Flow

interface TickerDataSource {
    fun getTicker(marketList: List<String>): Flow<Ticker>
}
