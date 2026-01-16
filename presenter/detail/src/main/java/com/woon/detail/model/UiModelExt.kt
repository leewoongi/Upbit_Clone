package com.woon.detail.model

import com.woon.domain.candle.entity.Candle

internal fun Candle.toUiModel() : CandleUiModel{
    return CandleUiModel(
        high = high.toDouble(),
        low = low.toDouble(),
        open = open.toDouble(),
        trade = trade.toDouble()
    )
}