package com.woon.detail.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woon.core.ext.formatToIso8601
import com.woon.detail.ui.intent.DetailIntent
import com.woon.detail.ui.mapper.toChartCandle
import com.woon.detail.ui.state.DetailUiState
import com.woon.domain.candle.entity.constant.CandleType
import com.woon.domain.candle.exception.CandleException
import com.woon.domain.candle.usecase.GetHistoricalCandlesUseCase
import com.woon.domain.candle.usecase.ObserveRealtimeCandlesUseCase
import com.woon.domain.event.reporter.ErrorReporter
import com.woon.chart.core.model.candle.TradingCandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getHistoricalCandles: GetHistoricalCandlesUseCase,
    private val observeRealtimeCandles: ObserveRealtimeCandlesUseCase,
    private val errorReporter: ErrorReporter
) : ViewModel() {

    private val marketCode: String = checkNotNull(savedStateHandle["marketCode"])
    private val _historyCandle: MutableStateFlow<Set<TradingCandle>> =
        MutableStateFlow(sortedSetOf(compareBy { it.timestamp }))
    private val _candleType = MutableStateFlow(CandleType.MINUTE_1)
    val candleType: StateFlow<CandleType> = _candleType.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _realtimeCandle = _candleType
        .flatMapLatest { type ->
            if (type.supportsWebSocket) {
                observeRealtimeCandles.invoke(
                    marketCode = marketCode,
                    candleType = type
                ).catch { e ->
                    errorReporter.report(e, SCREEN_NAME)
                }
            } else {
                emptyFlow()
            }
        }

    val uiState = combine(
        _candleType,
        _realtimeCandle,
        _historyCandle,
        _errorMessage
    ) { type, realtimeCandle, historyCandles, errorMessage ->
        when {
            errorMessage != null -> DetailUiState.Error(errorMessage)
            historyCandles.isEmpty() -> DetailUiState.Loading
            else -> {
                val candles = if (type.supportsWebSocket) {
                    historyCandles.toMutableList().apply {
                        if (isNotEmpty()) {
                            set(lastIndex, realtimeCandle.toChartCandle())
                        }
                    }
                } else {
                    historyCandles
                }

                DetailUiState.Success(
                    marketCode = marketCode,
                    candles = candles.toList()
                )
            }
        }
    }.catch { e ->
        errorReporter.report(e, SCREEN_NAME)
        emit(DetailUiState.Error(getErrorMessage(e)))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DetailUiState.Loading
    )

    init {
        loadCandle()
    }

    private fun loadCandle() {
        if (_isLoading.value) return
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val newCandles = getHistoricalCandles(
                    marketCode = marketCode,
                    candleType = _candleType.value,
                    to = _historyCandle.value.firstOrNull()?.timestamp?.formatToIso8601()
                ).map {
                    it.toChartCandle()
                }

                if (newCandles.isNotEmpty()) {
                    _historyCandle.value = (newCandles + _historyCandle.value)
                        .toSortedSet(compareBy { it.timestamp })
                }
            } catch (e: Throwable) {
                errorReporter.report(e, SCREEN_NAME)
                _errorMessage.value = getErrorMessage(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onIntent(intent: DetailIntent) {
        when (intent) {
            is DetailIntent.ChangeTimeFrame -> {
                _candleType.value = intent.candleType
                _historyCandle.value = sortedSetOf(compareBy { it.timestamp })
                _errorMessage.value = null
                loadCandle()
            }

            is DetailIntent.LoadCandle -> {
                loadCandle()
            }
        }
    }

    private fun getErrorMessage(e: Throwable): String {
        return when (e) {
            is CandleException -> e.message
            else -> "알 수 없는 오류가 발생했습니다"
        }
    }

    companion object {
        private const val SCREEN_NAME = "DetailScreen"
    }
}
