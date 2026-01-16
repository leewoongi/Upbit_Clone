package com.woon.detail.ui.state

import com.woon.domain.candle.entity.constant.Market
import com.woon.detail.model.CandleUiModel

sealed class DetailUiState {
    data class Success(
        val market: Market,
        val uiModel: List<CandleUiModel>,
        val priceRange: Pair<Double, Double>,
        val viewportPriceRange: Pair<Double, Double>? = null
    ) : DetailUiState()
    data object Loading: DetailUiState()
}