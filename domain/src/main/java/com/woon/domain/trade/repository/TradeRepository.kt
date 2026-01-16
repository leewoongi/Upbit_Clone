package com.woon.domain.trade.repository

import com.woon.domain.trade.entity.Trade
import kotlinx.coroutines.flow.Flow

interface TradeRepository {

    suspend fun connect()

    suspend fun disconnect()

    fun observeTrades(codes: List<String>): Flow<Trade>
}
