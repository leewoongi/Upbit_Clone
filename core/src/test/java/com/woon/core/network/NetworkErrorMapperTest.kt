package com.woon.core.network

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

/**
 * NetworkErrorMapper 테스트
 *
 * 각종 예외가 올바른 NetworkError 타입으로 매핑되는지 검증
 */
class NetworkErrorMapperTest {

    // ===================================
    // Timeout 매핑 테스트
    // ===================================

    @Test
    fun `SocketTimeoutException should map to TIMEOUT`() {
        val exception = SocketTimeoutException("Read timed out")

        val error = NetworkErrorMapper.map(exception)

        assertEquals(NetworkError.Type.TIMEOUT, error.type)
        assertTrue(error.isRetryable)
    }

    @Test
    fun `InterruptedIOException with timeout message should map to TIMEOUT`() {
        val exception = java.io.InterruptedIOException("timeout")

        val error = NetworkErrorMapper.map(exception)

        assertEquals(NetworkError.Type.TIMEOUT, error.type)
    }

    // ===================================
    // DNS 매핑 테스트
    // ===================================

    @Test
    fun `UnknownHostException should map to DNS`() {
        val exception = UnknownHostException("Unable to resolve host")

        val error = NetworkErrorMapper.map(exception)

        assertEquals(NetworkError.Type.DNS, error.type)
        assertTrue(error.isRetryable)
    }

    // ===================================
    // SSL 매핑 테스트
    // ===================================

    @Test
    fun `SSLHandshakeException should map to SSL`() {
        val exception = SSLHandshakeException("Handshake failed")

        val error = NetworkErrorMapper.map(exception)

        assertEquals(NetworkError.Type.SSL, error.type)
        assertFalse(error.isRetryable)  // SSL 에러는 재시도 불가
    }

    // ===================================
    // HTTP 에러 매핑 테스트
    // ===================================

    @Test
    fun `HTTP 429 should map to RATE_LIMIT`() {
        val response = Response.error<Any>(429, okhttp3.ResponseBody.create(null, ""))
        val exception = HttpException(response)

        val error = NetworkErrorMapper.map(exception)

        assertEquals(NetworkError.Type.RATE_LIMIT, error.type)
        assertTrue(error.isRetryable)
    }

    @Test
    fun `HTTP 500 should map to SERVER`() {
        val response = Response.error<Any>(500, okhttp3.ResponseBody.create(null, ""))
        val exception = HttpException(response)

        val error = NetworkErrorMapper.map(exception)

        assertEquals(NetworkError.Type.SERVER, error.type)
        assertTrue(error.isRetryable)
    }

    @Test
    fun `HTTP 400 should map to CLIENT`() {
        val response = Response.error<Any>(400, okhttp3.ResponseBody.create(null, ""))
        val exception = HttpException(response)

        val error = NetworkErrorMapper.map(exception)

        assertEquals(NetworkError.Type.CLIENT, error.type)
        assertFalse(error.isRetryable)  // 클라이언트 에러는 재시도 불가
    }

    // ===================================
    // IO 에러 매핑 테스트
    // ===================================

    @Test
    fun `IOException should map to IO`() {
        val exception = java.io.IOException("Connection reset")

        val error = NetworkErrorMapper.map(exception)

        assertEquals(NetworkError.Type.IO, error.type)
        assertTrue(error.isRetryable)
    }

    @Test
    fun `IOException with timeout message should map to TIMEOUT`() {
        val exception = java.io.IOException("Read timeout")

        val error = NetworkErrorMapper.map(exception)

        assertEquals(NetworkError.Type.TIMEOUT, error.type)
    }

    // ===================================
    // Parse 에러 매핑 테스트
    // ===================================

    @Test
    fun `JsonSyntaxException should map to PARSE`() {
        val exception = com.google.gson.JsonSyntaxException("Unexpected token")

        val error = NetworkErrorMapper.map(exception)

        assertEquals(NetworkError.Type.PARSE, error.type)
        assertFalse(error.isRetryable)
    }

    // ===================================
    // Unknown 에러 매핑 테스트
    // ===================================

    @Test
    fun `Unknown exception should map to UNKNOWN`() {
        val exception = RuntimeException("Something unexpected")

        val error = NetworkErrorMapper.map(exception)

        assertEquals(NetworkError.Type.UNKNOWN, error.type)
        assertFalse(error.isRetryable)
    }

    // ===================================
    // 확장 함수 테스트
    // ===================================

    @Test
    fun `toNetworkError extension should work`() {
        val exception = SocketTimeoutException("timeout")

        val error = exception.toNetworkError()

        assertEquals(NetworkError.Type.TIMEOUT, error.type)
    }

    // ===================================
    // userMessage 테스트
    // ===================================

    @Test
    fun `userMessage should return friendly message`() {
        val timeoutError = NetworkError.timeout()
        val dnsError = NetworkError.dns()
        val rateLimitError = NetworkError.rateLimit()

        assertTrue(timeoutError.userMessage.contains("지연"))
        assertTrue(dnsError.userMessage.contains("찾을 수 없습니다"))
        assertTrue(rateLimitError.userMessage.contains("많습니다"))
    }
}
