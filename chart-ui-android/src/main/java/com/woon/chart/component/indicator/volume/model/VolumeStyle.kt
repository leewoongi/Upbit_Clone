package com.woon.chart.component.indicator.volume.model

import androidx.compose.ui.graphics.Color

data class VolumeStyle(
    val risingColor: Color = Color(0xFF4CAF50),
    val fallingColor: Color = Color(0xFFF44336),
    val spacing: Float = 0.2f
)