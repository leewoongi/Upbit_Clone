package com.woon.chart.component.indicator.grid

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.woon.chart.component.indicator.grid.model.GridConfig
import com.woon.chart.ui.TradingChartState

fun DrawScope.drawGrid(
    state: TradingChartState,
    config: GridConfig
) {
    val model = config.model
    val style = config.style

    if (state.priceRange <= 0.0) return

    val interval = state.priceRange / model.lineCount

    // 가로선
    for (i in 0..model.lineCount) {
        val price = state.minPrice + interval * i
        val y = state.priceToY(price)
        drawLine(
            color = style.lineColor,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = style.strokeWidth
        )
    }

    // 세로선
    val firstCandle = state.visibleCandles.firstOrNull()
    val lastCandle = state.visibleCandles.lastOrNull()
    if (firstCandle != null && lastCandle != null && state.visibleCandles.size >= 2) {
        val startTime = firstCandle.timestamp
        val endTime = lastCandle.timestamp
        val gridInterval = state.gridIntervalMs

        var time = ((startTime / gridInterval) + 1) * gridInterval
        while (time <= endTime) {
            val x = state.timestampToScreenX(time)
            if (x in 0f..size.width) {
                drawLine(
                    color = style.lineColor,
                    start = Offset(x, 0f),
                    end = Offset(x, size.height),
                    strokeWidth = style.strokeWidth
                )
            }
            time += gridInterval
        }
    }
}
