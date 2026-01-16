package com.woon.datasource.remote.http.market

import com.woon.datasource.remote.http.market.response.MarketResponse

interface MarketDataSource {

    suspend fun getMarkets(): List<MarketResponse>
}
