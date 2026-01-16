package com.woon.repository.market

import com.woon.datasource.remote.http.market.MarketDataSource
import com.woon.domain.market.entity.Market
import com.woon.domain.market.repository.MarketRepository
import com.woon.repository.market.mapper.toDomain
import javax.inject.Inject

class MarketRepositoryImpl @Inject constructor(
    private val marketDataSource: MarketDataSource
) : MarketRepository {

    override suspend fun getMarkets(): List<Market> {
        return marketDataSource.getMarkets()
            .map { it.toDomain() }
    }
}
