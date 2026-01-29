package com.woon.chart.component.indicator.ichimokucloud.model

import androidx.compose.ui.graphics.Color
import com.woon.chart.core.model.candle.TradingCandle
import com.woon.chart.core.model.candle.extension.toIchimoku
import com.woon.chart.core.model.indicator.ichimokucloud.IchimokuCloud
import com.woon.chart.core.model.indicator.ichimokucloud.IchimokuSettings

data class IchimokuConfig(
    val settings: IchimokuSettings,
    internal val style: IchimokuStyle,
    val points: List<IchimokuCloud> = emptyList()
) {
    val enabled: Boolean get() = settings.enabled

    companion object {
        fun builder() = Builder()
    }

    class Builder {
        private var enabled: Boolean = false
        private var tenkanPeriod: Int = 9
        private var kijunPeriod: Int = 26
        private var senkouBPeriod: Int = 52
        private var displacement: Int = 26

        private var tenkanColor: Color = Color(0xFFE91E63)
        private var kijunColor: Color = Color(0xFF2196F3)
        private var senkouSpanAColor: Color = Color(0xFF4CAF50)
        private var senkouSpanBColor: Color = Color(0xFFFF5722)
        private var chikouColor: Color = Color(0xFF9C27B0)
        private var cloudBullishColor: Color = Color(0x4D4CAF50)
        private var cloudBearishColor: Color = Color(0x4DFF5722)
        private var strokeWidth: Float = 1.5f

        private var candles: List<TradingCandle> = emptyList()

        fun enabled(enabled: Boolean) = apply { this.enabled = enabled }
        fun tenkanPeriod(period: Int) = apply { this.tenkanPeriod = period }
        fun kijunPeriod(period: Int) = apply { this.kijunPeriod = period }
        fun senkouBPeriod(period: Int) = apply { this.senkouBPeriod = period }
        fun displacement(displacement: Int) = apply { this.displacement = displacement }

        fun tenkanColor(color: Color) = apply { this.tenkanColor = color }
        fun kijunColor(color: Color) = apply { this.kijunColor = color }
        fun senkouSpanAColor(color: Color) = apply { this.senkouSpanAColor = color }
        fun senkouSpanBColor(color: Color) = apply { this.senkouSpanBColor = color }
        fun chikouColor(color: Color) = apply { this.chikouColor = color }
        fun cloudBullishColor(color: Color) = apply { this.cloudBullishColor = color }
        fun cloudBearishColor(color: Color) = apply { this.cloudBearishColor = color }
        fun strokeWidth(width: Float) = apply { this.strokeWidth = width }

        internal fun candles(candles: List<TradingCandle>) = apply { this.candles = candles }

        fun build(): IchimokuConfig {
            val points = if (enabled && candles.isNotEmpty()) {
                candles.toIchimoku(tenkanPeriod, kijunPeriod, senkouBPeriod)
            } else emptyList()

            return IchimokuConfig(
                settings = IchimokuSettings(
                    enabled = enabled,
                    tenkanPeriod = tenkanPeriod,
                    kijunPeriod = kijunPeriod,
                    senkouBPeriod = senkouBPeriod,
                    displacement = displacement
                ),
                style = IchimokuStyle(
                    tenkanColor = tenkanColor,
                    kijunColor = kijunColor,
                    senkouSpanAColor = senkouSpanAColor,
                    senkouSpanBColor = senkouSpanBColor,
                    chikouColor = chikouColor,
                    cloudBullishColor = cloudBullishColor,
                    cloudBearishColor = cloudBearishColor,
                    strokeWidth = strokeWidth
                ),
                points = points
            )
        }
    }
}