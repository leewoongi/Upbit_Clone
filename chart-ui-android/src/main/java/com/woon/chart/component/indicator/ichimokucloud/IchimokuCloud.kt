package com.woon.chart.component.indicator.ichimokucloud

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.woon.chart.component.indicator.ichimokucloud.model.IchimokuConfig
import com.woon.chart.ui.TradingChartState

fun DrawScope.drawIchimokuCloud(
    state: TradingChartState,
    config: IchimokuConfig
) {
    if (!config.enabled || config.points.isEmpty()) return

    val style = config.style
    val displacement = config.settings.displacement
    val startIndex = state.visibleStartIndex
    val endIndex = state.visibleEndIndex.coerceAtMost(config.points.size)

    if (startIndex >= endIndex) return

    // 1. 구름대
    drawKumo(state, config, startIndex, endIndex, displacement)

    // 2. 전환선
    drawTenkanSen(state, config, startIndex, endIndex)

    // 3. 기준선
    drawKijunSen(state, config, startIndex, endIndex)

    // 4. 선행스팬A
    drawSenkouSpanA(state, config, startIndex, endIndex, displacement)

    // 5. 선행스팬B
    drawSenkouSpanB(state, config, startIndex, endIndex, displacement)

    // 6. 후행스팬
    drawChikouSpan(state, config, startIndex, endIndex, displacement)
}

private fun DrawScope.drawKumo(
    state: TradingChartState,
    config: IchimokuConfig,
    startIndex: Int,
    endIndex: Int,
    displacement: Int
) {
    val style = config.style
    val points = config.points

    val cloudStartIndex = (startIndex - displacement).coerceAtLeast(0)
    val cloudEndIndex = (endIndex - displacement).coerceAtMost(points.size - 1)

    if (cloudStartIndex >= cloudEndIndex) return

    for (i in cloudStartIndex until cloudEndIndex) {
        val current = points[i]
        val next = points[i + 1]

        if (current.senkouSpanA <= 0 || current.senkouSpanB <= 0) continue
        if (next.senkouSpanA <= 0 || next.senkouSpanB <= 0) continue

        val currentDisplayIndex = i + displacement
        val nextDisplayIndex = i + 1 + displacement

        val x1 = state.indexToScreenX(currentDisplayIndex)
        val x2 = state.indexToScreenX(nextDisplayIndex)

        val currentYA = state.priceToY(current.senkouSpanA)
        val currentYB = state.priceToY(current.senkouSpanB)
        val nextYA = state.priceToY(next.senkouSpanA)
        val nextYB = state.priceToY(next.senkouSpanB)

        val isBullish = current.senkouSpanA >= current.senkouSpanB
        val color = if (isBullish) style.cloudBullishColor else style.cloudBearishColor

        val path = Path().apply {
            moveTo(x1, currentYA)
            lineTo(x2, nextYA)
            lineTo(x2, nextYB)
            lineTo(x1, currentYB)
            close()
        }

        drawPath(path, color)
    }
}

private fun DrawScope.drawTenkanSen(
    state: TradingChartState,
    config: IchimokuConfig,
    startIndex: Int,
    endIndex: Int
) {
    val path = Path()
    var started = false

    for (i in startIndex until endIndex.coerceAtMost(config.points.size)) {
        val value = config.points[i].tenkanSen
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
        drawPath(path, config.style.tenkanColor, style = Stroke(width = config.style.strokeWidth))
    }
}

private fun DrawScope.drawKijunSen(
    state: TradingChartState,
    config: IchimokuConfig,
    startIndex: Int,
    endIndex: Int
) {
    val path = Path()
    var started = false

    for (i in startIndex until endIndex.coerceAtMost(config.points.size)) {
        val value = config.points[i].kijunSen
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
        drawPath(path, config.style.kijunColor, style = Stroke(width = config.style.strokeWidth))
    }
}

private fun DrawScope.drawSenkouSpanA(
    state: TradingChartState,
    config: IchimokuConfig,
    startIndex: Int,
    endIndex: Int,
    displacement: Int
) {
    val path = Path()
    var started = false

    val lineStartIndex = (startIndex - displacement).coerceAtLeast(0)
    val lineEndIndex = (endIndex - displacement).coerceAtMost(config.points.size)

    for (i in lineStartIndex until lineEndIndex) {
        val value = config.points[i].senkouSpanA
        if (value <= 0) continue

        val displayIndex = i + displacement
        val x = state.indexToScreenX(displayIndex)
        val y = state.priceToY(value)

        if (!started) {
            path.moveTo(x, y)
            started = true
        } else {
            path.lineTo(x, y)
        }
    }

    if (started) {
        drawPath(path, config.style.senkouSpanAColor, style = Stroke(width = config.style.strokeWidth))
    }
}

private fun DrawScope.drawSenkouSpanB(
    state: TradingChartState,
    config: IchimokuConfig,
    startIndex: Int,
    endIndex: Int,
    displacement: Int
) {
    val path = Path()
    var started = false

    val lineStartIndex = (startIndex - displacement).coerceAtLeast(0)
    val lineEndIndex = (endIndex - displacement).coerceAtMost(config.points.size)

    for (i in lineStartIndex until lineEndIndex) {
        val value = config.points[i].senkouSpanB
        if (value <= 0) continue

        val displayIndex = i + displacement
        val x = state.indexToScreenX(displayIndex)
        val y = state.priceToY(value)

        if (!started) {
            path.moveTo(x, y)
            started = true
        } else {
            path.lineTo(x, y)
        }
    }

    if (started) {
        drawPath(path, config.style.senkouSpanBColor, style = Stroke(width = config.style.strokeWidth))
    }
}

private fun DrawScope.drawChikouSpan(
    state: TradingChartState,
    config: IchimokuConfig,
    startIndex: Int,
    endIndex: Int,
    displacement: Int
) {
    val path = Path()
    var started = false

    val chikouStartIndex = (startIndex + displacement).coerceAtMost(config.points.size)
    val chikouEndIndex = (endIndex + displacement).coerceAtMost(config.points.size)

    for (i in chikouStartIndex until chikouEndIndex) {
        val value = config.points[i].chikouSpan
        if (value <= 0) continue

        val displayIndex = i - displacement
        val x = state.indexToScreenX(displayIndex)
        val y = state.priceToY(value)

        if (!started) {
            path.moveTo(x, y)
            started = true
        } else {
            path.lineTo(x, y)
        }
    }

    if (started) {
        drawPath(path, config.style.chikouColor, style = Stroke(width = config.style.strokeWidth))
    }
}