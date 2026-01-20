package com.woon.detail.ui.state

import com.woon.domain.candle.entity.Candle

sealed class DetailUiState {
    data object Loading : DetailUiState()

    data class Success(
        val marketCode: String,
        val candles: List<Candle> = emptyList()
    ) : DetailUiState()

    data class Error(val message: String) : DetailUiState()
}
