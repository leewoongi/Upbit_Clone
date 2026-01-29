package com.woon.chart.core.model.candle

import kotlin.math.abs

/**
 * 캔들 데이터 (플랫폼 독립적)
 *
 * 차트에 표시할 캔들의 가격 정보를 담는다.
 * 앱의 도메인 모델에서 변환하여 사용한다.
 *
 * @param timestamp 캔들 기준 시각 (Unix timestamp, ms)
 * @param open 시가
 * @param high 고가
 * @param low 저가
 * @param close 종가
 * @param volume 거래량
 */
data class TradingCandle(
    val timestamp: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Double
) {
    /** 양봉 여부 (종가 >= 시가) */
    val isRising: Boolean
        get() = close >= open

    /** 캔들 몸통 크기 */
    val bodySize: Double
        get() = abs(close - open)

    /** 캔들 전체 크기 (고가 - 저가) */
    val totalSize: Double
        get() = high - low

    /** 위 꼬리 크기 */
    val upperWick: Double
        get() = high - if (isRising) close else open

    /** 아래 꼬리 크기 */
    val lowerWick: Double
        get() = (if (isRising) open else close) - low
}