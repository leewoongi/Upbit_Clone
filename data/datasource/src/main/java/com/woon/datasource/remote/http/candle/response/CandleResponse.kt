package com.woon.datasource.remote.http.candle.response

import com.google.gson.annotations.SerializedName

/**
 * @param market 시장
 * @param candleDateTimeUtc UTC 기준 시각
 * @param candleDateTimeKst KST 기준 시각
 * @param openingPrice 시가
 * @param highPrice 고가
 * @param lowPrice 저가
 * @param tradePrice 종가 (현재 가격)
 * @param timestamp 마지막 거래 시간
 * @param candleAccTradePrice 누적 거래 대금
 * @param candleAccTradeVolume 누적 거래량
 * @param unit 분 단위 (분 단위부터 적용)
 */
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
    @SerializedName("unit") val unit: Int?
)
