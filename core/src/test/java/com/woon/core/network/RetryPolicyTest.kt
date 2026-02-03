package com.woon.core.network

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * RetryPolicy 테스트
 *
 * 지수 백오프 계산 및 재시도 로직 검증
 */
class RetryPolicyTest {

    // ===================================
    // 지연 시간 계산 테스트
    // ===================================

    @Test
    fun `getDelayMs should calculate exponential backoff`() {
        val policy = RetryPolicy(
            initialDelayMs = 1000,
            maxDelayMs = 10000,
            multiplier = 2.0
        )

        assertEquals(1000, policy.getDelayMs(0))  // 1000 * 2^0 = 1000
        assertEquals(2000, policy.getDelayMs(1))  // 1000 * 2^1 = 2000
        assertEquals(4000, policy.getDelayMs(2))  // 1000 * 2^2 = 4000
        assertEquals(8000, policy.getDelayMs(3))  // 1000 * 2^3 = 8000
    }

    @Test
    fun `getDelayMs should not exceed maxDelayMs`() {
        val policy = RetryPolicy(
            initialDelayMs = 1000,
            maxDelayMs = 5000,
            multiplier = 2.0
        )

        assertEquals(5000, policy.getDelayMs(10))  // should be capped at 5000
    }

    // ===================================
    // shouldRetry 테스트
    // ===================================

    @Test
    fun `shouldRetry should return true for retryable error within max retries`() {
        val policy = RetryPolicy(maxRetries = 2)
        val retryableError = NetworkError.timeout()

        assertTrue(policy.shouldRetry(0, retryableError))
        assertTrue(policy.shouldRetry(1, retryableError))
        assertFalse(policy.shouldRetry(2, retryableError))  // exceeded max
    }

    @Test
    fun `shouldRetry should return false for non-retryable error`() {
        val policy = RetryPolicy(maxRetries = 3)
        val nonRetryableError = NetworkError.client(400)

        assertFalse(policy.shouldRetry(0, nonRetryableError))
    }

    // ===================================
    // 사전 정의 정책 테스트
    // ===================================

    @Test
    fun `NONE policy should have zero retries`() {
        assertEquals(0, RetryPolicy.NONE.maxRetries)
    }

    @Test
    fun `DEFAULT policy should have standard values`() {
        val default = RetryPolicy.DEFAULT

        assertEquals(2, default.maxRetries)
        assertEquals(1000L, default.initialDelayMs)
        assertEquals(10_000L, default.maxDelayMs)
    }

    @Test
    fun `AGGRESSIVE policy should have faster retries`() {
        val aggressive = RetryPolicy.AGGRESSIVE

        assertEquals(3, aggressive.maxRetries)
        assertEquals(500L, aggressive.initialDelayMs)
    }

    // ===================================
    // retryWithPolicy 테스트
    // ===================================

    @Test
    fun `retryWithPolicy should succeed on first try`() = runTest {
        var callCount = 0

        val result = retryWithPolicy(RetryPolicy.DEFAULT) {
            callCount++
            "success"
        }

        assertTrue(result.isSuccess)
        assertEquals("success", result.getOrNull())
        assertEquals(1, callCount)
    }

    @Test
    fun `retryWithPolicy should retry on failure`() = runTest {
        var callCount = 0
        val policy = RetryPolicy(maxRetries = 2, initialDelayMs = 10)  // 짧은 지연

        val result = retryWithPolicy(policy) {
            callCount++
            if (callCount < 3) {
                throw java.net.SocketTimeoutException("timeout")
            }
            "success"
        }

        assertTrue(result.isSuccess)
        assertEquals(3, callCount)  // 초기 1회 + 재시도 2회
    }

    @Test
    fun `retryWithPolicy should fail after max retries`() = runTest {
        var callCount = 0
        val policy = RetryPolicy(maxRetries = 2, initialDelayMs = 10)

        val result = retryWithPolicy(policy) {
            callCount++
            throw java.net.SocketTimeoutException("timeout")
        }

        assertTrue(result.isFailure)
        assertEquals(3, callCount)  // 초기 1회 + 재시도 2회
    }

    @Test
    fun `retryWithPolicy should not retry non-retryable errors`() = runTest {
        var callCount = 0
        val policy = RetryPolicy(maxRetries = 3, initialDelayMs = 10)

        val result = retryWithPolicy(policy) {
            callCount++
            throw retrofit2.HttpException(
                retrofit2.Response.error<Any>(400, okhttp3.ResponseBody.create(null, ""))
            )
        }

        assertTrue(result.isFailure)
        assertEquals(1, callCount)  // 재시도 없이 즉시 실패
    }

    @Test
    fun `retryWithPolicy should call onRetry callback`() = runTest {
        var retryAttempts = mutableListOf<Int>()
        val policy = RetryPolicy(maxRetries = 2, initialDelayMs = 10)

        retryWithPolicy(
            policy = policy,
            onRetry = { attempt, _, _ -> retryAttempts.add(attempt) }
        ) {
            throw java.net.SocketTimeoutException("timeout")
        }

        assertEquals(listOf(1, 2), retryAttempts)
    }
}
