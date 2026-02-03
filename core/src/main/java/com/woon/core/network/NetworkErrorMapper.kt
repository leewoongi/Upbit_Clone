package com.woon.core.network

import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException
import javax.net.ssl.SSLHandshakeException

/**
 * Raw Exception을 NetworkError로 매핑하는 유틸리티
 */
object NetworkErrorMapper {

    /**
     * Throwable을 NetworkError로 변환
     */
    fun map(throwable: Throwable): NetworkError {
        // Retrofit HttpException 동적 처리 (retrofit 의존성 없이)
        if (throwable.javaClass.name == "retrofit2.HttpException") {
            return mapRetrofitHttpException(throwable)
        }

        return when (throwable) {
            // Timeout 계열
            is SocketTimeoutException -> NetworkError.timeout(throwable)
            is InterruptedIOException -> {
                if (throwable.message?.contains("timeout", ignoreCase = true) == true) {
                    NetworkError.timeout(throwable)
                } else {
                    NetworkError.io(throwable)
                }
            }

            // DNS 실패
            is UnknownHostException -> NetworkError.dns(throwable)

            // SSL/TLS 에러
            is SSLHandshakeException -> NetworkError.ssl(throwable)
            is SSLException -> NetworkError.ssl(throwable)

            // HTTP 에러 (core module의 HttpException)
            is HttpException -> mapHttpException(throwable)

            // 일반 IO 에러
            is java.io.IOException -> {
                when {
                    throwable.message?.contains("timeout", ignoreCase = true) == true ->
                        NetworkError.timeout(throwable)
                    throwable.message?.contains("ssl", ignoreCase = true) == true ->
                        NetworkError.ssl(throwable)
                    throwable.message?.contains("connection", ignoreCase = true) == true ->
                        NetworkError.io(throwable)
                    else -> NetworkError.io(throwable)
                }
            }

            // JSON 파싱 에러
            is com.google.gson.JsonSyntaxException,
            is com.google.gson.JsonParseException -> NetworkError.parse(throwable)

            // 알 수 없는 에러
            else -> NetworkError.unknown(throwable)
        }
    }

    /**
     * Core HttpException 에러 코드별 매핑
     */
    private fun mapHttpException(e: HttpException): NetworkError {
        val code = e.code()
        val retryAfter = e.response()?.headers?.get("Retry-After")?.toLongOrNull()
        val retryAfterMs = retryAfter?.times(1000)

        return when (code) {
            // Rate Limit
            429 -> NetworkError.rateLimit(retryAfterMs, e)

            // Client Errors (4xx)
            in 400..499 -> NetworkError.client(code, e)

            // Server Errors (5xx)
            in 500..599 -> NetworkError.server(code, e)

            else -> NetworkError.unknown(e)
        }
    }

    /**
     * Retrofit HttpException 동적 매핑 (리플렉션 사용)
     */
    private fun mapRetrofitHttpException(throwable: Throwable): NetworkError {
        return try {
            val codeMethod = throwable.javaClass.getMethod("code")
            val code = codeMethod.invoke(throwable) as Int

            // Retry-After 헤더 추출 시도
            var retryAfterMs: Long? = null
            try {
                val responseMethod = throwable.javaClass.getMethod("response")
                val response = responseMethod.invoke(throwable)
                if (response != null) {
                    val headersMethod = response.javaClass.getMethod("headers")
                    val headers = headersMethod.invoke(response)
                    if (headers != null) {
                        val getMethod = headers.javaClass.getMethod("get", String::class.java)
                        val retryAfter = getMethod.invoke(headers, "Retry-After") as? String
                        retryAfterMs = retryAfter?.toLongOrNull()?.times(1000)
                    }
                }
            } catch (_: Exception) {
                // Retry-After 헤더 추출 실패 무시
            }

            when (code) {
                429 -> NetworkError.rateLimit(retryAfterMs, throwable)
                in 400..499 -> NetworkError.client(code, throwable)
                in 500..599 -> NetworkError.server(code, throwable)
                else -> NetworkError.unknown(throwable)
            }
        } catch (_: Exception) {
            NetworkError.unknown(throwable)
        }
    }

    /**
     * OkHttp/Retrofit Call 실패 메시지로부터 에러 타입 추론
     */
    fun mapFromMessage(message: String?, cause: Throwable? = null): NetworkError {
        val msg = message?.lowercase() ?: return NetworkError.unknown(cause)

        return when {
            msg.contains("timeout") -> NetworkError.timeout(cause)
            msg.contains("unable to resolve host") || msg.contains("unknown host") -> NetworkError.dns(cause)
            msg.contains("ssl") || msg.contains("handshake") || msg.contains("certificate") -> NetworkError.ssl(cause)
            msg.contains("connection") || msg.contains("socket") -> NetworkError.io(cause)
            msg.contains("429") || msg.contains("rate limit") || msg.contains("too many") -> NetworkError.rateLimit(cause = cause)
            else -> NetworkError.unknown(cause)
        }
    }
}

/**
 * Throwable 확장 함수
 */
fun Throwable.toNetworkError(): NetworkError = NetworkErrorMapper.map(this)
