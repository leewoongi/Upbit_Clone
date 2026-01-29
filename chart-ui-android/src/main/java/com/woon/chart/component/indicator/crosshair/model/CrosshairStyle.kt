package com.woon.chart.component.indicator.crosshair.model

import androidx.compose.ui.graphics.Color

data class CrosshairStyle(
    val color: Color,
    val strokeWidth: Float,
    val dashOn: Float,
    val dashOff: Float
)