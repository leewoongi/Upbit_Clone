package com.woon.repository.ticker

import com.woon.datasource.remote.socket.ticker.TickerDataSource
import com.woon.domain.ticker.entity.Ticker
import com.woon.domain.ticker.repository.TickerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TickerRepositoryImpl @Inject constructor(
    private val tickerDataSource: TickerDataSource
) : TickerRepository {

    override fun observeTickers(codes: List<String>): Flow<Ticker> {
        return tickerDataSource.getTicker(codes)
    }
}
