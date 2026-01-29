package com.woon.detail.ui.mapper

import com.woon.domain.candle.entity.Candle
import com.woon.chart.core.model.candle.TradingCandle

fun Candle.toChartCandle() = TradingCandle(
    timestamp = this.timestamp.time,
    open = this.open.value,
    high = this.high.value,
    low = this.low.value,
    close = this.close.value,
    volume = this.accTradeVolume
)