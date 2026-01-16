package com.woon.domain.trade.service

import com.woon.domain.trade.entity.Trade
import com.woon.domain.trade.repository.TradeRepository
import com.woon.domain.trade.usecase.ObserveTradesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTradesService @Inject constructor(
    private val tradeRepository: TradeRepository
) : ObserveTradesUseCase {

    override suspend fun connect() {
        tradeRepository.connect()
    }

    override suspend fun disconnect() {
        tradeRepository.disconnect()
    }

    override fun invoke(codes: List<String>): Flow<Trade> {
        return tradeRepository.observeTrades(codes)
    }
}
