package com.woon.domain.market.usecase

import com.woon.domain.market.entity.Market

/**
 * 마켓 목록 조회 UseCase
 */
interface GetMarketsUseCase {

    /**
     * 전체 마켓 목록 조회
     * @return 마켓 목록
     */
    suspend operator fun invoke(): List<Market>

    /**
     * KRW 마켓만 조회
     * @return KRW 마켓 목록
     */
    suspend fun getKrwMarkets(): List<Market>

    /**
     * BTC 마켓만 조회
     * @return BTC 마켓 목록
     */
    suspend fun getBtcMarkets(): List<Market>

    /**
     * USDT 마켓만 조회
     * @return USDT 마켓 목록
     */
    suspend fun getUsdtMarkets(): List<Market>
}
