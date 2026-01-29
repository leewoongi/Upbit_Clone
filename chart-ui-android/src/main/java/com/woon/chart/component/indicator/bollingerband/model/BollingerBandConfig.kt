package com.woon.chart.component.indicator.bollingerband.model

import androidx.compose.ui.graphics.Color
import com.woon.chart.component.indicator.IndicatorBuilder
import com.woon.chart.core.model.candle.TradingCandle
import com.woon.chart.core.model.candle.extension.toBollingerBand
import com.woon.chart.core.model.indicator.bollingerband.BollingerBand
import com.woon.chart.core.model.indicator.bollingerband.BollingerBandSettings

data class BollingerBandConfig(
    val settings: BollingerBandSettings,
    val style: BollingerBandStyle,
    val points: List<BollingerBand> = emptyList()
) {
    val enabled: Boolean get() = settings.enabled

    class Builder: IndicatorBuilder {
        // Settings
        private var enabled: Boolean = false
        private var period: Int = 20
        private var multiplier: Float = 2f

        // Style
        private var upperColor: Color = Color(0xFF2196F3)
        private var middleColor: Color = Color(0xFFFFA726)
        private var lowerColor: Color = Color(0xFF2196F3)
        private var strokeWidth: Float = 1f

        // Data
        private var candles: List<TradingCandle> = emptyList()

        fun enabled(enabled: Boolean) = apply { this.enabled = enabled }
        fun period(period: Int) = apply { this.period = period }
        fun multiplier(multiplier: Float) = apply { this.multiplier = multiplier }
        fun upperColor(color: Color) = apply { this.upperColor = color }
        fun middleColor(color: Color) = apply { this.middleColor = color }
        fun lowerColor(color: Color) = apply { this.lowerColor = color }
        fun strokeWidth(width: Float) = apply { this.strokeWidth = width }
        fun candles(candles: List<TradingCandle>) = apply { this.candles = candles }

        override fun build(): BollingerBandConfig {
            val points = if (enabled && candles.isNotEmpty()) { candles.toBollingerBand() } else emptyList()

            return BollingerBandConfig(
                settings = BollingerBandSettings(enabled, period, multiplier),
                style = BollingerBandStyle(upperColor, middleColor, lowerColor, strokeWidth),
                points = points
            )
        }
    }

    companion object {
        fun builder() = Builder()
    }
}