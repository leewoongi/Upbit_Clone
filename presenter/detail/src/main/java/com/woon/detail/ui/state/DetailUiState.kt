package com.woon.detail.ui.state

import com.woon.chart.core.model.candle.TradingCandle

sealed class DetailUiState {
    data object Loading : DetailUiState()

    data class Success(
        val marketCode: String,
        val candles: List<TradingCandle> = emptyList()
    ) : DetailUiState()

    data class Error(val message: String) : DetailUiState()
}
