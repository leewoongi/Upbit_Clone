package com.woon.model.uistate

import com.woon.model.constant.SortOrder
import com.woon.model.constant.SortType
import com.woon.model.uimodel.CoinUiModel

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val coins: List<CoinUiModel>,
        val sortType: SortType = SortType.VOLUME,
        val sortOrder: SortOrder = SortOrder.DESC
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}