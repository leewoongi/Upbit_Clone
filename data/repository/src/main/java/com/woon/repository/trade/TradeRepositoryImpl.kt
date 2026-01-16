package com.woon.repository.trade

import com.woon.domain.trade.entity.Trade
import com.woon.domain.trade.repository.TradeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class TradeRepositoryImpl @Inject constructor() : TradeRepository {

    override suspend fun connect() {
        // TODO: Implement after network module is ready
    }

    override suspend fun disconnect() {
        // TODO: Implement after network module is ready
    }

    override fun observeTrades(codes: List<String>): Flow<Trade> {
        // TODO: Implement after network module is ready
        return emptyFlow()
    }
}
