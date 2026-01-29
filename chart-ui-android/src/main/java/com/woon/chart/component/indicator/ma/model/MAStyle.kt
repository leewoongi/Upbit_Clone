package com.woon.chart.component.indicator.ma.model

import androidx.compose.ui.graphics.Color
import com.woon.chart.core.model.indicator.ma.MA
import com.woon.chart.core.model.indicator.ma.MAType

data class MAStyle(
    val enabled: Boolean = true,
    val period: Int = 20,
    val type: MAType = MAType.SMA,
    val color: Color = Color.White,
    val strokeWidth: Float = 1.5f,
    val points: List<MA> = emptyList()
) {
    companion object {
        fun builder() = Builder()
    }

    class Builder {
        private var enabled: Boolean = true
        private var period: Int = 20
        private var type: MAType = MAType.SMA
        private var color: Color = Color.White
        private var strokeWidth: Float = 1.5f

        fun enabled(enabled: Boolean) = apply { this.enabled = enabled }
        fun period(period: Int) = apply { this.period = period }
        fun type(type: MAType) = apply { this.type = type }
        fun color(color: Color) = apply { this.color = color }
        fun strokeWidth(width: Float) = apply { this.strokeWidth = width }

        fun build() = MAStyle(
            enabled = enabled,
            period = period,
            type = type,
            color = color,
            strokeWidth = strokeWidth
        )
    }
}