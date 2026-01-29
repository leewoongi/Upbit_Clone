package com.woon.detail.ui.intent

import com.woon.domain.candle.entity.constant.CandleType

sealed interface DetailIntent {
    data class ChangeTimeFrame(val candleType: CandleType) : DetailIntent
    data object LoadCandle : DetailIntent
}
