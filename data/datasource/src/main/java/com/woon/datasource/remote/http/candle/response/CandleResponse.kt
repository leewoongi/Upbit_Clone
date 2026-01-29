package com.woon.datasource.remote.http.candle.response

import com.google.gson.annotations.SerializedName
import com.woon.domain.candle.entity.Candle
import com.woon.domain.candle.entity.constant.CandleType
import com.woon.domain.money.entity.Money
import java.util.Date

data class CandleResponse(
    @SerializedName("market") val market: String,
    @SerializedName("candle_date_time_utc") val candleDateTimeUtc: String,
    @SerializedName("candle_date_time_kst") val candleDateTimeKst: String,
    @SerializedName("opening_price") val openingPrice: Double,
    @SerializedName("high_price") val highPrice: Double,
    @SerializedName("low_price") val lowPrice: Double,
    @SerializedName("trade_price") val tradePrice: Double,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("candle_acc_trade_price") val candleAccTradePrice: Double,
    @SerializedName("candle_acc_trade_volume") val candleAccTradeVolume: Double,
    @SerializedName("unit") val unit: Int? = null
) {
    fun toDomain(type: CandleType): Candle {
        return Candle(
            market = market,
            type = type,
            dateTime = candleDateTimeUtc,
            open = Money(openingPrice),
            high = Money(highPrice),
            low = Money(lowPrice),
            close = Money(tradePrice),
            timestamp = Date(timestamp),
            accTradePrice = Money(candleAccTradePrice),
            accTradeVolume = candleAccTradeVolume
        )
    }

    // 분봉용 (unit 사용)
    fun toDomain(): Candle {
        return Candle(
            market = market,
            type = unit?.let { CandleType.fromRestUnit(it) } ?: CandleType.MINUTE_1,
            dateTime = candleDateTimeUtc,
            open = Money(openingPrice),
            high = Money(highPrice),
            low = Money(lowPrice),
            close = Money(tradePrice),
            timestamp = Date(timestamp),
            accTradePrice = Money(candleAccTradePrice),
            accTradeVolume = candleAccTradeVolume
        )
    }
}
