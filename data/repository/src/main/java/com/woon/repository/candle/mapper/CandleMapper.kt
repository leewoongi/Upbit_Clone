package com.woon.repository.candle.mapper

import com.woon.datasource.remote.http.candle.response.CandleResponse
import com.woon.domain.money.entity.Money
import com.woon.domain.candle.entity.Candle
import com.woon.domain.candle.entity.constant.Market
import com.woon.repository.candle.extension.toDate

internal fun CandleResponse.toDomain(): Candle {
    return Candle(
        market = Market.fromCode(this.market) ?: Market.KRW_BTC,
        time = this.candleDateTimeUtc.toDate(),
        open = Money(this.openingPrice),
        high = Money(this.highPrice),
        low = Money(this.lowPrice),
        trade = Money(this.tradePrice),
        current = this.timestamp.toDate(),
        accPrice = Money(this.candleAccTradePrice),
        accVolume = this.candleAccTradeVolume
    )
}
