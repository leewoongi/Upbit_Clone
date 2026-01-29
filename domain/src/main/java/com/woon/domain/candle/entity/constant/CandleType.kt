package com.woon.domain.candle.entity.constant

enum class CandleType(
    val wsType: String,
    val restUnit: Int?,
    val displayName: String,
    val supportsWebSocket: Boolean = true
) {
    SECOND("candle.1s", null, "1초"),
    MINUTE_1("candle.1m", 1, "1분"),
    MINUTE_3("candle.3m", 3, "3분"),
    MINUTE_5("candle.5m", 5, "5분"),
    MINUTE_10("candle.10m", 10, "10분"),
    MINUTE_15("candle.15m", 15, "15분"),
    MINUTE_30("candle.30m", 30, "30분"),
    MINUTE_60("candle.60m", 60, "1시간"),
    MINUTE_240("candle.240m", 240, "4시간"),
    DAY("day", null, "일", supportsWebSocket = false),
    WEEK("week", null, "주", supportsWebSocket = false),
    MONTH("month", null, "월", supportsWebSocket = false);

    companion object {
        fun fromWsType(type: String): CandleType {
            return entries.find { it.wsType == type } ?: MINUTE_1
        }

        fun fromRestUnit(unit: Int): CandleType {
            return entries.find { it.restUnit == unit } ?: MINUTE_1
        }
    }
}