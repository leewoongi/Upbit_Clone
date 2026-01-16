package com.woon.detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woon.domain.candle.usecase.CalculatePriceRangeUseCase
import com.woon.domain.candle.usecase.GetCandleUseCase
import com.woon.detail.model.toUiModel
import com.woon.detail.ui.state.DetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel
@Inject constructor(
    private val getCandleUseCase: GetCandleUseCase,
    private val calculatePriceRangeUseCase: CalculatePriceRangeUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        getCandle()
    }


    private fun getCandle() {
        viewModelScope.launch {
            val result = getCandleUseCase(240)
            val market = result.first().market
            val uiModel = result.map { it.toUiModel() }

            val high = uiModel.maxOf { it.high }
            val low = uiModel.minOf { it.low }

            _uiState.value = DetailUiState.Success(
                market = market,
                uiModel = uiModel,
                priceRange = Pair(high, low)
            )
        }
    }
}