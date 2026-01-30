package com.woon.detail.ui.intent

import com.woon.domain.candle.entity.constant.CandleType

sealed interface DetailIntent {
    data class ChangeTimeFrame(val candleType: CandleType) : DetailIntent
    data object LoadCandle : DetailIntent
    data class ChartZoom(val zoomFactor: Float) : DetailIntent
    data class ChartScroll(val direction: String) : DetailIntent  // "LEFT" or "RIGHT"
    data class CrosshairToggle(val enabled: Boolean) : DetailIntent
}
