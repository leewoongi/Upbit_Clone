package com.woon.chart.component.indicator.bollingerband

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.woon.chart.component.indicator.bollingerband.model.BollingerBandConfig
import com.woon.chart.ui.TradingChartState

fun DrawScope.drawBollingerBand(
    state: TradingChartState,
    config: BollingerBandConfig
) {
    val points = config.points
    val style = config.style

    if (points.isEmpty() || state.visibleCandles.isEmpty()) return

    val visiblePoints = points
        .drop(state.visibleStartIndex)
        .take(state.visibleCandles.size)

    if (visiblePoints.isEmpty()) return

    val upperPath = Path()
    val middlePath = Path()
    val lowerPath = Path()

    visiblePoints.forEachIndexed { i, point ->
        if (point.upper == 0.0) return@forEachIndexed

        val index = state.visibleStartIndex + i
        val x = state.indexToScreenX(index)
        val upperY = state.priceToY(point.upper)
        val middleY = state.priceToY(point.middle)
        val lowerY = state.priceToY(point.lower)

        if (upperPath.isEmpty) {
            upperPath.moveTo(x, upperY)
            middlePath.moveTo(x, middleY)
            lowerPath.moveTo(x, lowerY)
        } else {
            upperPath.lineTo(x, upperY)
            middlePath.lineTo(x, middleY)
            lowerPath.lineTo(x, lowerY)
        }
    }

    drawPath(upperPath, style.upperColor, style = Stroke(style.strokeWidth))
    drawPath(middlePath, style.middleColor, style = Stroke(style.strokeWidth))
    drawPath(lowerPath, style.lowerColor, style = Stroke(style.strokeWidth))
}