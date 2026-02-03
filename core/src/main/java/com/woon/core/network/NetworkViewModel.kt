package com.woon.core.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 네트워크 요청이 포함된 ViewModel의 공통 기능
 *
 * - 표준화된 Loading/Success/Error 상태 관리
 * - 자동 재시도 (지수 백오프) 지원
 * - 수동 재시도 버튼 지원
 * - 요청 취소 및 중복 요청 방지
 *
 * 사용 예시:
 * ```
 * class MyViewModel : ViewModel() {
 *     private val networkHelper = NetworkViewModelHelper<MyData>()
 *     val uiState = networkHelper.state
 *
 *     fun loadData() {
 *         networkHelper.execute(viewModelScope) {
 *             myRepository.fetchData()
 *         }
 *     }
 *
 *     fun retry() {
 *         networkHelper.manualRetry(viewModelScope) {
 *             myRepository.fetchData()
 *         }
 *     }
 * }
 * ```
 */
class NetworkViewModelHelper<T>(
    private val retryPolicy: RetryPolicy = RetryPolicy.DEFAULT,
    private val onError: ((NetworkError) -> Unit)? = null
) {
    private val _state = MutableStateFlow<NetworkUiState<T>>(NetworkUiState.Idle)
    val state: StateFlow<NetworkUiState<T>> = _state.asStateFlow()

    private var currentJob: Job? = null
    private var lastBlock: (suspend () -> T)? = null
    private var retryState = RetryState()

    /**
     * 네트워크 요청 실행
     *
     * 이전 요청이 진행 중이면 취소하고 새 요청 시작.
     * 자동 재시도 활성화 (retryPolicy에 따라).
     *
     * @param scope CoroutineScope (보통 viewModelScope)
     * @param block 실행할 suspend 함수
     */
    fun execute(
        scope: kotlinx.coroutines.CoroutineScope,
        block: suspend () -> T
    ) {
        // 이전 요청 취소
        currentJob?.cancel()
        lastBlock = block
        retryState = RetryState()

        currentJob = scope.launch {
            executeWithRetry(block)
        }
    }

    /**
     * 자동 재시도 로직 포함 실행
     */
    private suspend fun executeWithRetry(block: suspend () -> T) {
        _state.value = NetworkUiState.Loading()

        val result = retryWithPolicy(
            policy = retryPolicy,
            onRetry = { attempt, delayMs, error ->
                retryState = retryState.copy(
                    attemptCount = attempt,
                    isAutoRetrying = true,
                    nextRetryDelayMs = delayMs
                )
                _state.value = NetworkUiState.Error(
                    error = error,
                    retryState = retryState
                )
            },
            block = block
        )

        result.fold(
            onSuccess = { data ->
                retryState = retryState.finishAutoRetry()
                _state.value = NetworkUiState.Success(data)
            },
            onFailure = { exception ->
                val error = when (exception) {
                    is NetworkException -> exception.error
                    else -> exception.toNetworkError()
                }

                retryState = retryState.finishAutoRetry()
                _state.value = NetworkUiState.Error(
                    error = error,
                    retryState = retryState
                )

                onError?.invoke(error)
            }
        )
    }

    /**
     * 수동 재시도 (사용자가 버튼 클릭)
     *
     * 자동 재시도가 소진된 후에도 수동 재시도 가능.
     */
    fun manualRetry(scope: kotlinx.coroutines.CoroutineScope) {
        val block = lastBlock ?: return

        currentJob?.cancel()
        // 수동 재시도 시 카운트 리셋하지 않음 (로깅 목적)
        retryState = retryState.copy(allowManualRetry = true)

        currentJob = scope.launch {
            _state.value = NetworkUiState.Loading()

            try {
                val data = block()
                _state.value = NetworkUiState.Success(data)
            } catch (e: Throwable) {
                val error = e.toNetworkError()
                _state.value = NetworkUiState.Error(
                    error = error,
                    retryState = retryState
                )
                onError?.invoke(error)
            }
        }
    }

    /**
     * 수동 재시도 (새 블록 지정)
     */
    fun manualRetry(
        scope: kotlinx.coroutines.CoroutineScope,
        block: suspend () -> T
    ) {
        lastBlock = block
        manualRetry(scope)
    }

    /**
     * 현재 요청 취소
     */
    fun cancel() {
        currentJob?.cancel()
        currentJob = null
    }

    /**
     * 새로고침 (Success 상태에서 다시 로드)
     *
     * 기존 데이터를 유지하면서 새로고침 인디케이터 표시
     */
    fun refresh(
        scope: kotlinx.coroutines.CoroutineScope,
        block: suspend () -> T
    ) {
        val currentData = (_state.value as? NetworkUiState.Success)?.data

        currentJob?.cancel()
        lastBlock = block
        retryState = RetryState()

        currentJob = scope.launch {
            // 기존 데이터가 있으면 isRefreshing으로 표시
            if (currentData != null) {
                _state.value = NetworkUiState.Success(currentData, isRefreshing = true)
            } else {
                _state.value = NetworkUiState.Loading()
            }

            try {
                val data = block()
                _state.value = NetworkUiState.Success(data)
            } catch (e: Throwable) {
                val error = e.toNetworkError()

                // 기존 데이터가 있으면 에러 토스트만 표시하고 데이터 유지
                if (currentData != null) {
                    _state.value = NetworkUiState.Success(currentData)
                    onError?.invoke(error)
                } else {
                    _state.value = NetworkUiState.Error(
                        error = error,
                        retryState = retryState
                    )
                }
            }
        }
    }

    /**
     * 상태 초기화
     */
    fun reset() {
        currentJob?.cancel()
        currentJob = null
        lastBlock = null
        retryState = RetryState()
        _state.value = NetworkUiState.Idle
    }

    /**
     * 현재 로딩 중인지
     */
    val isLoading: Boolean
        get() = _state.value is NetworkUiState.Loading

    /**
     * 현재 상태가 에러인지
     */
    val isError: Boolean
        get() = _state.value is NetworkUiState.Error
}
