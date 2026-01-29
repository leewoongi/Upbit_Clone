package com.woon.chart.component.indicator.crosshair.model

import androidx.compose.ui.graphics.Color
import com.woon.chart.component.indicator.IndicatorBuilder
import com.woon.chart.core.model.indicator.crosshair.Crosshair

data class CrosshairConfig(
    val model: Crosshair,
    val style: CrosshairStyle
) {

    fun show(x: Float, y: Float) = copy(model = model.copy(enabled = true, x = x, y = y))
    fun moveTo(x: Float, y: Float) = if (model.enabled) copy(model = model.copy(x = x, y = y)) else this

    fun hide() = copy(model = model.copy(enabled = false))

    class Builder : IndicatorBuilder {
        private var color: Color = Color(0xFF000000)
        private var strokeWidth: Float = 1f
        private var dashOn: Float = 10f
        private var dashOff: Float = 10f

        fun color(color: Color) = apply { this.color = color }
        fun strokeWidth(width: Float) = apply { this.strokeWidth = width }
        fun dash(on: Float, off: Float) = apply { this.dashOn = on; this.dashOff = off }

        override fun build() = CrosshairConfig(
            model = Crosshair(),
            style = CrosshairStyle(
                color = color,
                strokeWidth = strokeWidth,
                dashOn = dashOn,
                dashOff = dashOff
            )
        )
    }

    companion object {
        fun builder() = Builder()
    }
}