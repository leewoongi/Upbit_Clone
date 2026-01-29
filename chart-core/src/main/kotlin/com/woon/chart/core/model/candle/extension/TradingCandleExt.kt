package com.woon.chart.core.model.candle.extension

import com.woon.chart.core.model.candle.TradingCandle
import com.woon.chart.core.model.indicator.bollingerband.BollingerBand
import com.woon.chart.core.model.indicator.ichimokucloud.IchimokuCloud
import com.woon.chart.core.model.indicator.ma.MA
import com.woon.chart.core.model.indicator.ma.MAType
import kotlin.math.sqrt

fun List<TradingCandle>.toBollingerBand(
    period: Int = 20,
    multiplier: Float = 2f
): List<BollingerBand> {
    if (size < period) return emptyList()

    return mapIndexed { index, _ ->
        if (index < period - 1) {
            BollingerBand(0.0, 0.0, 0.0)
        } else {
            val slice = subList(index - period + 1, index + 1)
            val closes = slice.map { it.close }
            val sma = closes.average()
            val std = sqrt(closes.map { (it - sma) * (it - sma) }.average())

            BollingerBand(
                upper = sma + multiplier * std,
                middle = sma,
                lower = sma - multiplier * std
            )
        }
    }
}

/** 기간 내 최고가 + 최저가의 중간값 */
private fun List<TradingCandle>.midPoint(
    startIndex: Int,
    period: Int
): Double {
    if (startIndex < period - 1) return 0.0

    val slice = subList(startIndex - period + 1, startIndex + 1)
    val high = slice.maxOf { it.high }
    val low = slice.minOf { it.low }
    return (high + low) / 2
}

/** 캔들 리스트 → 이치모쿠 리스트 */
fun List<TradingCandle>.toIchimoku(
    tenkanPeriod: Int = 9,
    kijunPeriod: Int = 26,
    senkouBPeriod: Int = 52
): List<IchimokuCloud> {
    if (size < senkouBPeriod) return emptyList()

    return mapIndexed { index, candle ->
        val tenkanSen = midPoint(index, tenkanPeriod)
        val kijunSen = midPoint(index, kijunPeriod)
        val senkouSpanA = if (tenkanSen > 0 && kijunSen > 0) {
            (tenkanSen + kijunSen) / 2
        } else 0.0
        val senkouSpanB = midPoint(index, senkouBPeriod)
        val chikouSpan = candle.close

        IchimokuCloud(
            tenkanSen = tenkanSen,
            kijunSen = kijunSen,
            senkouSpanA = senkouSpanA,
            senkouSpanB = senkouSpanB,
            chikouSpan = chikouSpan
        )
    }
}

/**
 * 캔들 리스트 → 이동평균 리스트
 */
fun List<TradingCandle>.toMovingAverage(
    period: Int,
    type: MAType = MAType.SMA
): List<MA> {
    if (size < period) return emptyList()

    return when (type) {
        MAType.SMA -> toSMA(period)
        MAType.EMA -> toEMA(period)
        MAType.WMA -> toWMA(period)
    }
}

/**
 * 단순 이동평균 (Simple Moving Average)
 */
private fun List<TradingCandle>.toSMA(
    period: Int
): List<MA> {
    return mapIndexed { index, _ ->
        if (index < period - 1) {
            MA(0.0)
        } else {
            val slice = subList(index - period + 1, index + 1)
            val avg = slice.map { it.close }.average()
            MA(avg)
        }
    }
}

/**
 * 지수 이동평균 (Exponential Moving Average)
 */
private fun List<TradingCandle>.toEMA(
    period: Int
): List<MA> {
    val k = 2.0 / (period + 1)
    val result = mutableListOf<MA>()

    forEachIndexed { index, candle ->
        if (index < period - 1) {
            result.add(MA(0.0))
        } else if (index == period - 1) {
            // 첫 EMA = SMA
            val slice = subList(0, period)
            val sma = slice.map { it.close }.average()
            result.add(MA(sma))
        } else {
            val prevEMA = result[index - 1].average
            val ema = candle.close * k + prevEMA * (1 - k)
            result.add(MA(ema))
        }
    }

    return result
}

/**
 * 가중 이동평균 (Weighted Moving Average)
 */
private fun List<TradingCandle>.toWMA(
    period: Int
): List<MA> {
    val weights = (1..period).sum()

    return mapIndexed { index, _ ->
        if (index < period - 1) {
            MA(0.0)
        } else {
            val slice = subList(index - period + 1, index + 1)
            var weightedSum = 0.0
            slice.forEachIndexed { i, candle ->
                weightedSum += candle.close * (i + 1)
            }
            MA(weightedSum / weights)
        }
    }
}