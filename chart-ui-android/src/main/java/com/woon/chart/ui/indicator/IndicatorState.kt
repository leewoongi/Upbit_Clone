package com.woon.chart.ui.indicator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.woon.chart.component.indicator.bollingerband.model.BollingerBandConfig
import com.woon.chart.component.indicator.crosshair.model.CrosshairConfig
import com.woon.chart.component.indicator.grid.model.GridConfig
import com.woon.chart.component.indicator.ichimokucloud.model.IchimokuConfig
import com.woon.chart.component.indicator.ma.model.MAConfig
import com.woon.chart.component.indicator.volume.model.VolumeConfig
import com.woon.chart.core.model.indicator.bollingerband.extension.getPriceRange
import com.woon.chart.core.model.indicator.ichimokucloud.extension.getPriceRange

class IndicatorState(
    private val getVisibleStartIndex: () -> Int,
    private val getVisibleCount: () -> Int
) {
    var gridConfig by mutableStateOf(GridConfig.Companion.builder().build())
    var crosshairConfig by mutableStateOf(CrosshairConfig.Companion.builder().build())
    var bollingerBandConfig by mutableStateOf(BollingerBandConfig.Companion.builder().build())
    var ichimokuConfig by mutableStateOf(IchimokuConfig.Companion.builder().build())
    var maConfig by mutableStateOf(MAConfig.builder().build())
    var volumeConfig by mutableStateOf(VolumeConfig.builder().build())

    fun update(config: Any) {
        when (config) {
            is GridConfig -> gridConfig = config
            is CrosshairConfig -> crosshairConfig = CrosshairConfig(
                model = crosshairConfig.model,
                style = config.style
            )
            is BollingerBandConfig -> bollingerBandConfig = config
            is IchimokuConfig -> ichimokuConfig = config
            is MAConfig -> maConfig = config
            is VolumeConfig -> volumeConfig = config
        }
    }

    fun toggleCrosshair(x: Float, y: Float) {
        crosshairConfig = if (crosshairConfig.model.enabled) {
            crosshairConfig.hide()
        } else {
            crosshairConfig.show(x, y)
        }
    }

    fun moveCrosshair(x: Float, y: Float) {
        crosshairConfig = crosshairConfig.moveTo(x, y)
    }

    val priceRange: Pair<Double, Double>?
        get() {
            val ranges = listOfNotNull(
                bollingerBandConfig.points.getPriceRange(
                    getVisibleStartIndex(),
                    getVisibleCount()
                ),
                ichimokuConfig.points.getPriceRange(
                    getVisibleStartIndex(),
                    getVisibleCount(),
                    ichimokuConfig.settings.displacement
                ),
                maConfig.getPriceRange(
                    getVisibleStartIndex(),
                    getVisibleCount()
                )
            )
            if (ranges.isEmpty()) return null
            return Pair(
                ranges.minOf { it.first },
                ranges.maxOf { it.second }
            )
        }
}