package com.woon.model.mapper

import com.woon.core.ext.reverseParts
import com.woon.domain.market.entity.Market
import com.woon.domain.ticker.entity.Ticker
import com.woon.model.uimodel.CoinUiModel

fun Ticker.toCoinUiModel(
    market: Market?
): CoinUiModel {
    return CoinUiModel(
        id = code,
        name = market?.koreanName ?: "",
        symbol = code.reverseParts(),
        price = tradePrice.toPriceFormat(),
        changeRate = "${if (signedChangeRate >= 0) "+" else ""}${String.format("%.2f", signedChangeRate * 100)}%",
        changeType = change,
        volume = accTradePrice24h.toVolumeFormat(),
        rawPrice = tradePrice.toDouble(),
        rawChangeRate = signedChangeRate,
        rawVolume = accTradePrice24h.toDouble()
    )
}

