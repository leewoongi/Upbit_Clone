package com.woon.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLException
import javax.inject.Inject

class HttpEventInterceptor @Inject constructor() : Interceptor {

    private var listener: HttpEventListener? = null

    fun setListener(listener: HttpEventListener?) {
        this.listener = listener
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath
        val method = request.method
        val isEventEndpoint = path.contains("event")

        var lastException: IOException? = null
        var retryCount = 0

        while (retryCount <= MAX_RETRY_COUNT) {
            val startTime = System.currentTimeMillis()

            try {
                val response = chain.proceed(request)
                val duration = System.currentTimeMillis() - startTime

                // 성공 기록 (/event 제외)
                if (!isEventEndpoint) {
                    listener?.onHttpEvent(
                        method = method,
                        path = path,
                        statusCode = response.code,
                        durationMs = duration
                    )
                }

                // 서버 에러(5xx)면 재시도
                if (response.code >= 500 && retryCount < MAX_RETRY_COUNT) {
                    response.close()
                    retryCount++
                    Thread.sleep(RETRY_DELAY_MS * retryCount)
                    continue
                }

                return response

            } catch (e: SocketTimeoutException) {
                val duration = System.currentTimeMillis() - startTime
                lastException = e
                logError(method, path, e, duration, retryCount, isEventEndpoint)
                retryCount++

                if (retryCount <= MAX_RETRY_COUNT) {
                    Thread.sleep(RETRY_DELAY_MS * retryCount)
                }

            } catch (e: SSLException) {
                val duration = System.currentTimeMillis() - startTime
                lastException = e
                logError(method, path, e, duration, retryCount, isEventEndpoint)
                // SSL 에러는 재시도해도 의미없음
                throw e

            } catch (e: IOException) {
                val duration = System.currentTimeMillis() - startTime
                lastException = e
                logError(method, path, e, duration, retryCount, isEventEndpoint)
                retryCount++

                if (retryCount <= MAX_RETRY_COUNT) {
                    Thread.sleep(RETRY_DELAY_MS * retryCount)
                }
            }
        }

        // 모든 재시도 실패
        throw lastException ?: IOException("Unknown network error")
    }

    private fun logError(
        method: String,
        path: String,
        e: Exception,
        durationMs: Long,
        retryCount: Int,
        isEventEndpoint: Boolean
    ) {
        if (isEventEndpoint) return

        val exceptionType = when (e) {
            is SocketTimeoutException -> "SOCKET_TIMEOUT"
            is SSLException -> "SSL_ERROR"
            else -> e.javaClass.simpleName
        }

        println("[HttpEventInterceptor] $exceptionType: $method $path (${durationMs}ms, retry=$retryCount)")

        listener?.onHttpError(
            method = method,
            path = path,
            exceptionType = exceptionType,
            message = e.message ?: "Unknown error",
            durationMs = durationMs,
            retryCount = retryCount
        )
    }

    companion object {
        private const val MAX_RETRY_COUNT = 2
        private const val RETRY_DELAY_MS = 1000L
    }
}
