package com.woon.model.uistate

import com.woon.model.uimodel.SortUiState

data class HomeScreenUiState(
    val dataState: HomeDataState = HomeDataState.Loading,
    val sort: SortUiState = SortUiState()
)