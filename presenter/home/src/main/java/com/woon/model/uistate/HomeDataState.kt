package com.woon.model.uistate

import com.woon.model.uimodel.CoinUiModel

sealed class HomeDataState {
    data object Loading : HomeDataState()
    data class Success(val coins: List<CoinUiModel>) : HomeDataState()
    data class Error(val message: String) : HomeDataState()
    data object Empty : HomeDataState()
}