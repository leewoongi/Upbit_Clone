package com.woon.chart.component.indicator.bollingerband.model

import androidx.compose.ui.graphics.Color

data class BollingerBandStyle(
    val upperColor: Color,
    val middleColor: Color,
    val lowerColor: Color,
    val strokeWidth: Float
)