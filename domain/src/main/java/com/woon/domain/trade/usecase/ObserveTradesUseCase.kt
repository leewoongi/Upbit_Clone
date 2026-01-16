package com.woon.domain.trade.usecase

import com.woon.domain.trade.entity.Trade
import kotlinx.coroutines.flow.Flow

/**
 * 체결 데이터 실시간 구독 UseCase
 */
interface ObserveTradesUseCase {

    /**
     * WebSocket 연결
     */
    suspend fun connect()

    /**
     * WebSocket 연결 해제
     */
    suspend fun disconnect()

    /**
     * 체결 데이터 구독
     * @param codes 마켓 코드 목록 (예: ["KRW-BTC", "KRW-ETH"])
     * @return 체결 데이터 Flow
     */
    operator fun invoke(codes: List<String>): Flow<Trade>
}
