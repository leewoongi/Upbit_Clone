package com.woon.core.network

/**
 * 네트워크 요청을 포함하는 화면의 표준 UI 상태
 *
 * @param T 성공 시 데이터 타입
 */
sealed interface NetworkUiState<out T> {

    /**
     * 초기 상태 (아직 로딩 시작 전)
     */
    data object Idle : NetworkUiState<Nothing>

    /**
     * 로딩 중
     *
     * @property message 로딩 메시지 (선택)
     * @property progress 진행률 0.0~1.0 (선택)
     */
    data class Loading(
        val message: String? = null,
        val progress: Float? = null
    ) : NetworkUiState<Nothing>

    /**
     * 성공
     *
     * @property data 응답 데이터
     * @property isRefreshing 새로고침 중 여부 (pull-to-refresh)
     */
    data class Success<T>(
        val data: T,
        val isRefreshing: Boolean = false
    ) : NetworkUiState<T>

    /**
     * 에러
     *
     * @property error 네트워크 에러 정보
     * @property retryState 재시도 상태
     */
    data class Error(
        val error: NetworkError,
        val retryState: RetryState = RetryState()
    ) : NetworkUiState<Nothing> {

        /**
         * 재시도 가능 여부
         */
        val canRetry: Boolean
            get() = error.isRetryable || retryState.allowManualRetry

        /**
         * 자동 재시도 중 여부
         */
        val isAutoRetrying: Boolean
            get() = retryState.isAutoRetrying
    }
}

/**
 * 재시도 상태
 *
 * @property attemptCount 현재까지 시도 횟수
 * @property maxAutoRetries 최대 자동 재시도 횟수
 * @property isAutoRetrying 자동 재시도 진행 중 여부
 * @property nextRetryDelayMs 다음 재시도까지 대기 시간
 * @property allowManualRetry 수동 재시도 허용 여부
 */
data class RetryState(
    val attemptCount: Int = 0,
    val maxAutoRetries: Int = MAX_AUTO_RETRIES,
    val isAutoRetrying: Boolean = false,
    val nextRetryDelayMs: Long = 0L,
    val allowManualRetry: Boolean = true
) {
    /**
     * 자동 재시도 가능 여부
     */
    val canAutoRetry: Boolean
        get() = attemptCount < maxAutoRetries && !isAutoRetrying

    /**
     * 자동 재시도 소진 여부
     */
    val autoRetryExhausted: Boolean
        get() = attemptCount >= maxAutoRetries

    /**
     * 다음 재시도 시도
     */
    fun nextAttempt(): RetryState = copy(
        attemptCount = attemptCount + 1,
        isAutoRetrying = true
    )

    /**
     * 자동 재시도 완료 (성공 또는 실패)
     */
    fun finishAutoRetry(): RetryState = copy(isAutoRetrying = false)

    companion object {
        const val MAX_AUTO_RETRIES = 2
    }
}

/**
 * NetworkUiState 유틸리티 확장 함수
 */
val <T> NetworkUiState<T>.isLoading: Boolean
    get() = this is NetworkUiState.Loading

val <T> NetworkUiState<T>.isSuccess: Boolean
    get() = this is NetworkUiState.Success

val <T> NetworkUiState<T>.isError: Boolean
    get() = this is NetworkUiState.Error

val <T> NetworkUiState<T>.dataOrNull: T?
    get() = (this as? NetworkUiState.Success)?.data

val <T> NetworkUiState<T>.errorOrNull: NetworkError?
    get() = (this as? NetworkUiState.Error)?.error
