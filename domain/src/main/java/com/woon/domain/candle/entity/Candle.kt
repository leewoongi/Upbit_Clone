package com.woon.domain.candle.entity

import com.woon.domain.candle.entity.constant.Market
import com.woon.domain.money.entity.Money
import java.util.Date

/**
 * @param market 시장
 * @param time 시각
 * @param open 시가
 * @param high 고가
 * @param low 저가
 * @param trade 종가 (현재 가격)
 * @param current 현재 거래된 시간
 * @param accPrice 누적 거래 대금
 * @param accVolume 누적 거래량
 */
data class Candle(
    val market: Market,
    val time: Date,
    val open: Money,
    val high: Money,
    val low: Money,
    val trade: Money,
    val current: Date,
    val accPrice: Money,
    val accVolume: Double
)
