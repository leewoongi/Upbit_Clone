package com.woon.repository.ticker

import com.woon.datasource.remote.socket.ticker.TickerDataSource
import com.woon.domain.ticker.entity.Ticker
import com.woon.domain.ticker.exception.TickerException
import com.woon.domain.ticker.repository.TickerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class TickerRepositoryImpl @Inject constructor(
    private val tickerDataSource: TickerDataSource
) : TickerRepository {

    override fun observeTickers(codes: List<String>): Flow<Ticker> {
        return tickerDataSource.getTicker(codes)
            .catch { e ->
                if (e is TickerException) throw e
                throw TickerException.WebSocketConnectionException(cause = e)
            }
    }
}
