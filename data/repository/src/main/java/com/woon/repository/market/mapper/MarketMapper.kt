package com.woon.repository.market.mapper

import com.woon.datasource.remote.http.market.response.MarketCautionResponse
import com.woon.datasource.remote.http.market.response.MarketResponse
import com.woon.domain.market.entity.Market
import com.woon.domain.market.entity.MarketCaution

fun MarketResponse.toDomain(): Market {
    return Market(
        code = market,
        koreanName = koreanName,
        englishName = englishName,
        marketWarning = marketEvent?.warning ?: false,
        caution = marketEvent?.caution?.toDomain()
    )
}

fun MarketCautionResponse.toDomain(): MarketCaution {
    return MarketCaution(
        priceFluctuations = priceFluctuations,
        tradingVolumeSoaring = tradingVolumeSoaring,
        depositAmountSoaring = depositAmountSoaring,
        globalPriceDifferences = globalPriceDifferences,
        concentrationOfSmallAccounts = concentrationOfSmallAccounts
    )
}
