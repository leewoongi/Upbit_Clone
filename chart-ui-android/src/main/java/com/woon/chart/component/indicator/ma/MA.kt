package com.woon.chart.component.indicator.ma

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.woon.chart.component.indicator.ma.model.MAConfig
import com.woon.chart.component.indicator.ma.model.MAStyle
import com.woon.chart.ui.TradingChartState

fun DrawScope.drawMovingAverage(
    state: TradingChartState,
    config: MAConfig
) {
    if (!config.enabled || config.lines.isEmpty()) return

    config.lines.forEach { line ->
        if (line.enabled && line.points.isNotEmpty()) {
            drawMALine(state, line)
        }
    }
}

private fun DrawScope.drawMALine(
    state: TradingChartState,
    line: MAStyle
) {
    val path = Path()
    var started = false

    val startIndex = state.visibleStartIndex
    val endIndex = state.visibleEndIndex.coerceAtMost(line.points.size)

    for (i in startIndex until endIndex) {
        val value = line.points[i].average
        if (value <= 0) continue

        val x = state.indexToScreenX(i)
        val y = state.priceToY(value)

        if (!started) {
            path.moveTo(x, y)
            started = true
        } else {
            path.lineTo(x, y)
        }
    }

    if (started) {
        drawPath(path, line.color, style = Stroke(width = line.strokeWidth))
    }
}