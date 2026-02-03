package com.woon.core.network

import kotlinx.coroutines.delay
import kotlin.math.min
import kotlin.math.pow

/**
 * 네트워크 요청 재시도 정책
 *
 * 지수 백오프(Exponential Backoff)를 사용한 자동 재시도를 제공한다.
 *
 * @property maxRetries 최대 재시도 횟수 (기본 2회)
 * @property initialDelayMs 초기 지연 시간 (기본 1초)
 * @property maxDelayMs 최대 지연 시간 (기본 10초)
 * @property multiplier 지연 시간 배수 (기본 2.0)
 */
data class RetryPolicy(
    val maxRetries: Int = DEFAULT_MAX_RETRIES,
    val initialDelayMs: Long = DEFAULT_INITIAL_DELAY_MS,
    val maxDelayMs: Long = DEFAULT_MAX_DELAY_MS,
    val multiplier: Double = DEFAULT_MULTIPLIER
) {
    /**
     * N번째 재시도의 지연 시간 계산 (지수 백오프)
     *
     * delay = min(initialDelay * (multiplier ^ attempt), maxDelay)
     *
     * @param attempt 시도 횟수 (0부터 시작)
     */
    fun getDelayMs(attempt: Int): Long {
        val exponentialDelay = initialDelayMs * multiplier.pow(attempt.toDouble())
        return min(exponentialDelay.toLong(), maxDelayMs)
    }

    /**
     * 재시도 가능 여부 확인
     */
    fun shouldRetry(attempt: Int, error: NetworkError): Boolean {
        if (attempt >= maxRetries) return false
        return error.isRetryable
    }

    companion object {
        const val DEFAULT_MAX_RETRIES = 2
        const val DEFAULT_INITIAL_DELAY_MS = 1000L     // 1초
        const val DEFAULT_MAX_DELAY_MS = 10_000L       // 10초
        const val DEFAULT_MULTIPLIER = 2.0

        /**
         * 재시도 없음
         */
        val NONE = RetryPolicy(maxRetries = 0)

        /**
         * 기본 정책 (2회, 지수 백오프)
         */
        val DEFAULT = RetryPolicy()

        /**
         * 공격적 재시도 (빠른 재시도, Rate Limit 대응)
         */
        val AGGRESSIVE = RetryPolicy(
            maxRetries = 3,
            initialDelayMs = 500L,
            maxDelayMs = 5000L,
            multiplier = 1.5
        )

        /**
         * 보수적 재시도 (느린 재시도, 서버 부하 감소)
         */
        val CONSERVATIVE = RetryPolicy(
            maxRetries = 2,
            initialDelayMs = 2000L,
            maxDelayMs = 30_000L,
            multiplier = 3.0
        )
    }
}

/**
 * 재시도 가능한 네트워크 요청 실행
 *
 * @param policy 재시도 정책
 * @param onRetry 재시도 시 콜백 (attempt, delay, error)
 * @param block 실행할 suspend 블록
 * @return 성공 결과 또는 마지막 실패
 */
suspend fun <T> retryWithPolicy(
    policy: RetryPolicy = RetryPolicy.DEFAULT,
    onRetry: ((attempt: Int, delayMs: Long, error: NetworkError) -> Unit)? = null,
    block: suspend () -> T
): Result<T> {
    var lastError: NetworkError? = null

    repeat(policy.maxRetries + 1) { attempt ->
        try {
            return Result.success(block())
        } catch (e: Throwable) {
            val networkError = e.toNetworkError()
            lastError = networkError

            // 재시도 불가능한 에러이면 즉시 실패
            if (!policy.shouldRetry(attempt, networkError)) {
                return Result.failure(NetworkException(networkError))
            }

            // 마지막 시도가 아니면 지연 후 재시도
            if (attempt < policy.maxRetries) {
                val delayMs = networkError.retryAfterMs ?: policy.getDelayMs(attempt)
                onRetry?.invoke(attempt + 1, delayMs, networkError)
                delay(delayMs)
            }
        }
    }

    return Result.failure(NetworkException(lastError ?: NetworkError.unknown()))
}

/**
 * Flow에서 사용하기 위한 재시도 래퍼
 */
suspend fun <T> withRetry(
    policy: RetryPolicy = RetryPolicy.DEFAULT,
    block: suspend () -> T
): T {
    return retryWithPolicy(policy, block = block).getOrThrow()
}

/**
 * NetworkError를 포함하는 예외
 */
class NetworkException(
    val error: NetworkError
) : Exception(error.message, error.cause)
