package com.woon.core.network

import okhttp3.Response

/**
 * HTTP 에러를 나타내는 Exception
 *
 * Retrofit의 HttpException과 유사한 인터페이스를 제공하여
 * core 모듈에서 retrofit 의존성 없이 HTTP 에러를 처리할 수 있게 합니다.
 */
class HttpException(
    private val code: Int,
    message: String? = null,
    private val response: Response? = null
) : RuntimeException(message ?: "HTTP $code") {

    /**
     * HTTP 상태 코드
     */
    fun code(): Int = code

    /**
     * OkHttp Response (nullable)
     */
    fun response(): Response? = response
}
