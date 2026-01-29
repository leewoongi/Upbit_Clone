package com.woon.chart.component.indicator.volume.model

import androidx.compose.ui.graphics.Color

data class VolumeConfig(
    val enabled: Boolean = false,
    val heightRatio: Float = 0.2f,
    val style: VolumeStyle
) {
    companion object {
        fun builder() = Builder()
    }

    class Builder {
        private var enabled: Boolean = false
        private var heightRatio: Float = 0.2f
        private var risingColor: Color = Color(0xFF4CAF50)
        private var fallingColor: Color = Color(0xFFF44336)
        private var spacing: Float = 0.2f

        fun enabled(enabled: Boolean) = apply { this.enabled = enabled }
        fun heightRatio(ratio: Float) = apply { this.heightRatio = ratio.coerceIn(0.1f, 0.5f) }
        fun risingColor(color: Color) = apply { this.risingColor = color }
        fun fallingColor(color: Color) = apply { this.fallingColor = color }
        fun spacing(spacing: Float) = apply { this.spacing = spacing }

        fun build() = VolumeConfig(
            enabled = enabled,
            heightRatio = heightRatio,
            style = VolumeStyle(
                risingColor = risingColor,
                fallingColor = fallingColor,
                spacing = spacing
            )
        )
    }
}