package com.woon.domain.market.repository

import com.woon.domain.market.entity.Market

interface MarketRepository {

    suspend fun getMarkets(): List<Market>
}
