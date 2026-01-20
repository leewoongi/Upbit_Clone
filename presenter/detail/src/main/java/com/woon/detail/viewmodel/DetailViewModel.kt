package com.woon.detail.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woon.detail.ui.state.DetailUiState
import com.woon.domain.candle.entity.Candle
import com.woon.domain.candle.entity.constant.CandleType
import com.woon.domain.candle.usecase.GetHistoricalCandlesUseCase
import com.woon.domain.candle.usecase.ObserveRealtimeCandlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getHistoricalCandles: GetHistoricalCandlesUseCase,
    private val observeRealtimeCandles: ObserveRealtimeCandlesUseCase
) : ViewModel() {

    private val marketCode: String = checkNotNull(savedStateHandle["marketCode"])

    private val _candleType = MutableStateFlow(CandleType.MINUTE_1)
    private val _realtimeCandle = observeRealtimeCandles.invoke(
        marketCode = marketCode,
        candleType = _candleType.value
    )

    val uiState = combine(
        _realtimeCandle
    ){

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DetailUiState.Loading
    )


    private fun observeCandlesFlow(candleType: CandleType) = flow {
        try {
            val candles = mutableListOf<Candle>()

            // 1. 과거 캔들 데이터 로드
            val historicalCandles = getHistoricalCandles(
                marketCode = marketCode,
                candleType = candleType,
            )
            candles.addAll(historicalCandles)
            emit(DetailUiState.Success(marketCode, candles.toList()))

            // 2. 실시간 캔들 구독하면서 누적
            observeRealtimeCandles(marketCode, candleType)
                .collect { newCandle ->
                    updateOrAppendCandle(candles, newCandle)
                    emit(DetailUiState.Success(marketCode, candles.toList()))
                }
        } catch (e: Exception) {
            emit(DetailUiState.Error(e.message ?: "Unknown error"))
        }
    }

    private fun updateOrAppendCandle(candles: MutableList<Candle>, newCandle: Candle) {
        val existingIndex = candles.indexOfFirst { it.dateTime == newCandle.dateTime }
        if (existingIndex >= 0) {
            // 같은 시간대 캔들이면 업데이트
            candles[existingIndex] = newCandle
        } else {
            // 새 캔들이면 추가
            candles.add(newCandle)
        }
    }

    fun changeCandleType(candleType: CandleType) {
        _candleType.value = candleType
    }
}
