package com.woon.domain.candle.entity.constant

enum class CandleType(
    val wsType: String,
    val restUnit: Int?
) {
    SECOND("candle.1s", null),
    MINUTE_1("candle.1m", 1),
    MINUTE_3("candle.3m", 3),
    MINUTE_5("candle.5m", 5),
    MINUTE_10("candle.10m", 10),
    MINUTE_15("candle.15m", 15),
    MINUTE_30("candle.30m", 30),
    MINUTE_60("candle.60m", 60),
    MINUTE_240("candle.240m", 240),
    DAY("day", null),
    WEEK("week", null),
    MONTH("month", null);

    companion object {
        fun fromWsType(type: String): CandleType {
            return entries.find { it.wsType == type } ?: MINUTE_1
        }

        fun fromRestUnit(unit: Int): CandleType {
            return entries.find { it.restUnit == unit } ?: MINUTE_1
        }
    }
}