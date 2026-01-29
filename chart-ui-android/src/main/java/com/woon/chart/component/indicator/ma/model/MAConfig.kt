package com.woon.chart.component.indicator.ma.model

import androidx.compose.ui.graphics.Color
import com.woon.chart.core.model.candle.TradingCandle
import com.woon.chart.core.model.candle.extension.toMovingAverage
import com.woon.chart.core.model.indicator.ma.MAType
import com.woon.chart.core.model.indicator.ma.extension.getPriceRange

data class MAConfig(
    val enabled: Boolean = false,
    val lines: List<MAStyle> = emptyList()
) {
    companion object {
        const val MAX_LINES = 6

        fun builder() = Builder()
    }

    fun getPriceRange(startIndex: Int, count: Int): Pair<Double, Double>? {
        val ranges = lines
            .filter { it.enabled && it.points.isNotEmpty() }
            .mapNotNull { it.points.getPriceRange(startIndex, count) }

        if (ranges.isEmpty()) return null

        return Pair(
            ranges.minOf { it.first },
            ranges.maxOf { it.second }
        )
    }

    class Builder {
        private var enabled: Boolean = false
        private val lineBuilders = mutableListOf<MAStyle.Builder>()
        private var candles: List<TradingCandle> = emptyList()

        fun enabled(enabled: Boolean) = apply { this.enabled = enabled }

        fun addLine(line: MAStyle.Builder) = apply {
            if (lineBuilders.size < MAX_LINES) {
                lineBuilders.add(line)
            }
        }

        fun addLine(
            period: Int,
            color: Color,
            type: MAType = MAType.SMA,
            strokeWidth: Float = 1.5f
        ) = apply {
            if (lineBuilders.size < MAX_LINES) {
                lineBuilders.add(
                    MAStyle.builder()
                        .period(period)
                        .color(color)
                        .type(type)
                        .strokeWidth(strokeWidth)
                )
            }
        }

        internal fun candles(candles: List<TradingCandle>) = apply {
            this.candles = candles
        }

        fun build(): MAConfig {
            val lines = if (enabled && candles.isNotEmpty()) {
                lineBuilders.map { builder ->
                    val lineConfig = builder.build()
                    val points = candles.toMovingAverage(lineConfig.period, lineConfig.type)
                    lineConfig.copy(points = points)
                }
            } else {
                lineBuilders.map { it.build() }
            }

            return MAConfig(
                enabled = enabled,
                lines = lines
            )
        }
    }
}
