package com.woon.chart.component.indicator.crosshair

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.woon.chart.component.indicator.crosshair.model.CrosshairConfig

fun DrawScope.drawCrossHair(
    config: CrosshairConfig
) {
    val model = config.model
    val style = config.style

    val dashEffect = PathEffect.dashPathEffect(
        floatArrayOf(style.dashOn, style.dashOff)
    )

    // 세로선
    drawLine(
        color = style.color,
        start = Offset(model.x, 0f),
        end = Offset(model.x, size.height),
        strokeWidth = style.strokeWidth,
        pathEffect = dashEffect
    )

    // 가로선
    drawLine(
        color = style.color,
        start = Offset(0f, model.y),
        end = Offset(size.width, model.y),
        strokeWidth = style.strokeWidth,
        pathEffect = dashEffect
    )
}