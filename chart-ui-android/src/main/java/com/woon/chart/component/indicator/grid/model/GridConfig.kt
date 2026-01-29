package com.woon.chart.component.indicator.grid.model

import androidx.compose.ui.graphics.Color
import com.woon.chart.component.indicator.IndicatorBuilder
import com.woon.chart.core.model.indicator.grid.Grid

data class GridConfig(
    val model: Grid,
    val style: GridStyle
) {
    class Builder : IndicatorBuilder {
        private var lineCount: Int = 5
        private var color: Color = Color(0xFF2A2A2A)
        private var strokeWidth: Float = 1f

        fun lineCount(count: Int) = apply { this.lineCount = count }
        fun color(color: Color) = apply { this.color = color }
        fun strokeWidth(width: Float) = apply { this.strokeWidth = width }

        override fun build() = GridConfig(
            model = Grid(lineCount),
            style = GridStyle(
                lineColor = color,
                strokeWidth = strokeWidth
            )
        )
    }

    companion object {
        fun builder() = Builder()
    }
}