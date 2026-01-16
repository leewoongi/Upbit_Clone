package com.woon.domain.market.service

import com.woon.domain.market.entity.Market
import com.woon.domain.market.repository.MarketRepository
import com.woon.domain.market.usecase.GetMarketsUseCase
import javax.inject.Inject

class GetMarketsService @Inject constructor(
    private val marketRepository: MarketRepository
) : GetMarketsUseCase {

    override suspend fun invoke(): List<Market> {
        return marketRepository.getMarkets()
    }

    override suspend fun getKrwMarkets(): List<Market> {
        return marketRepository.getMarkets()
            .filter { it.code.startsWith("KRW-") }
    }

    override suspend fun getBtcMarkets(): List<Market> {
        return marketRepository.getMarkets()
            .filter { it.code.startsWith("BTC-") }
    }

    override suspend fun getUsdtMarkets(): List<Market> {
        return marketRepository.getMarkets()
            .filter { it.code.startsWith("USDT-") }
    }
}
