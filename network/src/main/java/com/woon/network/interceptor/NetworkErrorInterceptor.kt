package com.woon.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * 네트워크 에러 인터셉터
 *
 * OkHttp 레벨에서 발생하는 네트워크 에러를 감지하고 로깅한다.
 * 상위 레이어에서 NetworkErrorMapper를 통해 공통 에러 모델로 변환된다.
 */
class NetworkErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startTime = System.currentTimeMillis()

        try {
            val response = chain.proceed(request)
            val duration = System.currentTimeMillis() - startTime

            // Rate Limit (429) 로깅
            if (response.code == 429) {
                val retryAfter = response.header("Retry-After")
                println("[NetworkError] Rate limit exceeded: ${request.url}, retryAfter=$retryAfter")
            }

            // 서버 에러 (5xx) 로깅
            if (response.code >= 500) {
                println("[NetworkError] Server error ${response.code}: ${request.url}, duration=${duration}ms")
            }

            return response
        } catch (e: SocketTimeoutException) {
            val duration = System.currentTimeMillis() - startTime
            println("[NetworkError] Timeout: ${request.url}, duration=${duration}ms")
            throw NetworkTimeoutException(
                url = request.url.toString(),
                method = request.method,
                durationMs = duration,
                cause = e
            )
        } catch (e: UnknownHostException) {
            println("[NetworkError] DNS failed: ${request.url}, host=${e.message}")
            throw NetworkDnsException(
                url = request.url.toString(),
                host = request.url.host,
                cause = e
            )
        } catch (e: SSLException) {
            println("[NetworkError] SSL error: ${request.url}, message=${e.message}")
            throw NetworkSslException(
                url = request.url.toString(),
                cause = e
            )
        } catch (e: IOException) {
            val duration = System.currentTimeMillis() - startTime
            println("[NetworkError] IO error: ${request.url}, message=${e.message}, duration=${duration}ms")
            throw NetworkIOException(
                url = request.url.toString(),
                method = request.method,
                message = e.message,
                cause = e
            )
        }
    }
}

/**
 * 네트워크 타임아웃 예외 (상세 정보 포함)
 */
class NetworkTimeoutException(
    val url: String,
    val method: String,
    val durationMs: Long,
    cause: Throwable
) : IOException("Request timeout after ${durationMs}ms: $method $url", cause)

/**
 * DNS 조회 실패 예외
 */
class NetworkDnsException(
    val url: String,
    val host: String,
    cause: Throwable
) : IOException("DNS lookup failed for host: $host", cause)

/**
 * SSL 에러 예외
 */
class NetworkSslException(
    val url: String,
    cause: Throwable
) : IOException("SSL error for: $url", cause)

/**
 * 일반 네트워크 IO 예외
 */
class NetworkIOException(
    val url: String,
    val method: String,
    message: String?,
    cause: Throwable
) : IOException("Network IO error: $method $url - $message", cause)
