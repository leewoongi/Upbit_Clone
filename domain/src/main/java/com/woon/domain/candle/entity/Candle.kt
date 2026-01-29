package com.woon.domain.candle.entity

import com.woon.domain.candle.entity.constant.CandleType
import com.woon.domain.money.entity.Money
import java.util.Date

/**
 * 캔들 데이터
 *
 * @param market 마켓 코드 (ex. "KRW-BTC")
 * @param type 캔들 타입 (1분봉, 5분봉 등)
 * @param dateTime 캔들 기준 시각 (ex. "2025-01-19T12:00:00")
 * @param open 시가
 * @param high 고가
 * @param low 저가
 * @param close 종가
 * @param timestamp 타임스탬프 (ms)
 * @param accTradePrice 누적 거래 대금
 * @param accTradeVolume 누적 거래량
 */
data class Candle(
    val market: String,
    val type: CandleType,
    val dateTime: String,
    val open: Money,
    val high: Money,
    val low: Money,
    val close: Money,
    val timestamp: Date,
    val accTradePrice: Money,
    val accTradeVolume: Double
)