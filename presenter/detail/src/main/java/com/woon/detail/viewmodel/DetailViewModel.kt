package com.woon.detail.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woon.core.ext.formatToIso8601
import com.woon.core.network.NetworkError
import com.woon.core.network.NetworkUiState
import com.woon.core.network.RetryPolicy
import com.woon.core.network.retryWithPolicy
import com.woon.core.network.toNetworkError
import com.woon.detail.ui.intent.DetailIntent
import com.woon.detail.ui.mapper.toChartCandle
import com.woon.detail.ui.state.DetailUiState
import com.woon.domain.breadcrumb.recorder.BreadcrumbRecorder
import com.woon.domain.candle.entity.constant.CandleType
import com.woon.domain.candle.exception.CandleException
import com.woon.domain.candle.manager.CandleRequestManager
import com.woon.domain.candle.manager.candleRequestKey
import com.woon.domain.candle.usecase.GetHistoricalCandlesUseCase
import com.woon.domain.candle.usecase.ObserveRealtimeCandlesUseCase
import com.woon.domain.event.reporter.ErrorReporter
import com.woon.chart.core.model.candle.TradingCandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
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
    private val errorReporter: ErrorReporter,
    private val breadcrumbRecorder: BreadcrumbRecorder,
    private val candleRequestManager: CandleRequestManager
) : ViewModel() {

    private val marketCode: String = checkNotNull(savedStateHandle["marketCode"])
    private val _historyCandle: MutableStateFlow<Set<TradingCandle>> =
        MutableStateFlow(sortedSetOf(compareBy { it.timestamp }))
    private val _candleType = MutableStateFlow(CandleType.MINUTE_1)
    val candleType: StateFlow<CandleType> = _candleType.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    private val _networkError = MutableStateFlow<NetworkError?>(null)
    private val _retryCount = MutableStateFlow(0)

    // 현재 로드 요청 Job (취소용)
    private var currentLoadJob: Job? = null

    // Throttle을 위한 마지막 기록 시간
    private var lastZoomRecordTime = 0L
    private var lastScrollRecordTime = 0L
    private var lastLoadRequestTime = 0L

    // 재시도 정책
    private val retryPolicy = RetryPolicy(
        maxRetries = MAX_AUTO_RETRIES,
        initialDelayMs = RETRY_INITIAL_DELAY_MS,
        maxDelayMs = RETRY_MAX_DELAY_MS,
        multiplier = RETRY_MULTIPLIER
    )

    init {
        breadcrumbRecorder.recordScreen(SCREEN_NAME, mapOf("marketCode" to marketCode))
        loadCandle()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _realtimeCandle = _candleType
        .flatMapLatest { type ->
            if (type.supportsWebSocket) {
                observeRealtimeCandles.invoke(
                    marketCode = marketCode,
                    candleType = type
                ).catch { e ->
                    errorReporter.report(e, SCREEN_NAME, feature = FEATURE_REALTIME_CANDLE)
                }
            } else {
                emptyFlow()
            }
        }

    val uiState = combine(
        _candleType,
        _realtimeCandle,
        _historyCandle,
        _networkError,
        _isLoading,
        _retryCount
    ) { params ->
        val type = params[0] as CandleType
        val realtimeCandle = params[1] as? com.woon.domain.candle.entity.Candle
        @Suppress("UNCHECKED_CAST")
        val historyCandles = params[2] as Set<TradingCandle>
        val networkError = params[3] as? NetworkError
        val isLoading = params[4] as Boolean
        val retryCount = params[5] as Int

        when {
            // 네트워크 에러 (재시도 정보 포함)
            networkError != null -> DetailUiState.Error(
                message = networkError.userMessage,
                networkError = networkError,
                canRetry = networkError.isRetryable,
                retryCount = retryCount,
                isAutoRetrying = retryCount < MAX_AUTO_RETRIES && networkError.isRetryable
            )
            // 로딩 중
            isLoading && historyCandles.isEmpty() -> DetailUiState.Loading
            // 데이터 없음
            historyCandles.isEmpty() -> DetailUiState.Empty
            // 성공
            else -> {
                val candles = if (type.supportsWebSocket && realtimeCandle != null) {
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
                    candles = candles.toList(),
                    isRefreshing = isLoading
                )
            }
        }
    }.catch { e ->
        errorReporter.report(e, SCREEN_NAME, feature = FEATURE_CANDLE_UI)
        emit(DetailUiState.Error(
            message = getErrorMessage(e),
            networkError = e.toNetworkError(),
            canRetry = true
        ))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DetailUiState.Loading
    )

    /**
     * 캔들 데이터 로드
     *
     * - 이전 요청이 진행 중이면 취소
     * - Throttle 적용 (300ms 내 중복 호출 방지)
     * - 자동 재시도 (지수 백오프, 최대 2회)
     * - 실패 시 수동 재시도 허용
     */
    private fun loadCandle() {
        // Throttle: 너무 빈번한 요청 방지
        val now = System.currentTimeMillis()
        if (now - lastLoadRequestTime < LOAD_THROTTLE_MS) {
            return
        }
        lastLoadRequestTime = now

        // 이미 로딩 중이면 스킵 (이전 요청 진행 중)
        if (_isLoading.value) return

        _isLoading.value = true
        _networkError.value = null
        _retryCount.value = 0

        // 이전 요청 취소
        currentLoadJob?.cancel()

        val requestKey = candleRequestKey(marketCode, _candleType.value)

        currentLoadJob = viewModelScope.launch {
            // 요청 매니저에 등록
            candleRequestManager.registerRequest(requestKey, coroutineContext[Job]!!)

            val result = retryWithPolicy(
                policy = retryPolicy,
                onRetry = { attempt, delayMs, error ->
                    _retryCount.value = attempt
                    breadcrumbRecorder.recordSystem(
                        name = "CandleRetry",
                        attrs = mapOf(
                            "attempt" to attempt.toString(),
                            "delay" to delayMs.toString(),
                            "errorType" to error.type.name
                        )
                    )
                }
            ) {
                getHistoricalCandles(
                    marketCode = marketCode,
                    candleType = _candleType.value,
                    to = _historyCandle.value.firstOrNull()?.timestamp?.formatToIso8601()
                ).map { it.toChartCandle() }
            }

            result.fold(
                onSuccess = { newCandles ->
                    if (newCandles.isNotEmpty()) {
                        _historyCandle.value = (newCandles + _historyCandle.value)
                            .toSortedSet(compareBy { it.timestamp })
                    }
                    _networkError.value = null
                },
                onFailure = { e ->
                    val networkError = e.toNetworkError()
                    _networkError.value = networkError
                    errorReporter.report(e, SCREEN_NAME, feature = FEATURE_CANDLE_LOAD)

                    // Fallback 이벤트 기록
                    errorReporter.reportFallback(
                        errorType = networkError.type.name,
                        feature = "Candle",
                        screen = SCREEN_NAME,
                        topFrameHint = "DetailViewModel.loadCandle",
                        extra = mapOf(
                            "marketCode" to marketCode,
                            "candleType" to _candleType.value.name,
                            "retryCount" to _retryCount.value.toString()
                        )
                    )
                }
            )

            _isLoading.value = false
        }
    }

    /**
     * 수동 재시도 (사용자가 재시도 버튼 클릭)
     */
    fun retry() {
        breadcrumbRecorder.recordClick("RetryButton", mapOf("feature" to "Candle"))
        _retryCount.value = 0  // 수동 재시도 시 카운트 리셋
        loadCandle()
    }

    fun onIntent(intent: DetailIntent) {
        when (intent) {
            is DetailIntent.ChangeTimeFrame -> {
                breadcrumbRecorder.recordClick(
                    "TimeFrameChange",
                    mapOf("from" to _candleType.value.name, "to" to intent.candleType.name)
                )
                _candleType.value = intent.candleType
                _historyCandle.value = sortedSetOf(compareBy { it.timestamp })
                _networkError.value = null
                loadCandle()
            }

            is DetailIntent.LoadCandle -> {
                breadcrumbRecorder.recordClick("ChartLoadMore")
                loadCandle()
            }

            is DetailIntent.ChartZoom -> {
                val now = System.currentTimeMillis()
                if (now - lastZoomRecordTime > THROTTLE_MS) {
                    lastZoomRecordTime = now
                    val direction = if (intent.zoomFactor > 1f) "IN" else "OUT"
                    breadcrumbRecorder.recordClick(
                        "ChartZoom",
                        mapOf("direction" to direction)
                    )
                }
            }

            is DetailIntent.ChartScroll -> {
                val now = System.currentTimeMillis()
                if (now - lastScrollRecordTime > THROTTLE_MS) {
                    lastScrollRecordTime = now
                    breadcrumbRecorder.recordClick(
                        "ChartScroll",
                        mapOf("direction" to intent.direction)
                    )
                }
            }

            is DetailIntent.CrosshairToggle -> {
                breadcrumbRecorder.recordClick(
                    "CrosshairToggle",
                    mapOf("enabled" to intent.enabled.toString())
                )
            }
        }
    }

    private fun getErrorMessage(e: Throwable): String {
        return when (e) {
            is CandleException -> e.message
            else -> e.toNetworkError().userMessage
        }
    }

    override fun onCleared() {
        super.onCleared()
        // 모든 진행 중인 요청 취소
        currentLoadJob?.cancel()
        viewModelScope.launch {
            candleRequestManager.cancelAllForMarket(marketCode)
        }
    }

    companion object {
        private const val SCREEN_NAME = "DetailScreen"
        private const val FEATURE_CANDLE_LOAD = "CandleLoad"
        private const val FEATURE_REALTIME_CANDLE = "RealtimeCandle"
        private const val FEATURE_CANDLE_UI = "CandleUI"
        private const val THROTTLE_MS = 500L  // 0.5초 throttle (UI 이벤트용)
        private const val LOAD_THROTTLE_MS = 300L  // 300ms throttle (API 요청용)

        // 재시도 정책 상수
        private const val MAX_AUTO_RETRIES = 2
        private const val RETRY_INITIAL_DELAY_MS = 1000L    // 1초
        private const val RETRY_MAX_DELAY_MS = 5000L        // 5초
        private const val RETRY_MULTIPLIER = 2.0
    }
}
