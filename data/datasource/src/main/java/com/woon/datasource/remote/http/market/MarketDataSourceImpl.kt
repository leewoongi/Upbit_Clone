package com.woon.datasource.remote.http.market

import com.woon.datasource.remote.http.market.api.MarketApi
import com.woon.datasource.remote.http.market.response.MarketResponse
import javax.inject.Inject

class MarketDataSourceImpl @Inject constructor(
    private val marketApi: MarketApi
) : MarketDataSource {

    override suspend fun getMarkets(): List<MarketResponse> {
        return marketApi.getMarkets(isDetails = true)
    }
}
