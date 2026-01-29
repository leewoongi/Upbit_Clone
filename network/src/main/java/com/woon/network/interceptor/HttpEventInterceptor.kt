package com.woon.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HttpEventInterceptor @Inject constructor() : Interceptor {

    private var listener: HttpEventListener? = null

    fun setListener(listener: HttpEventListener?) {
        this.listener = listener
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startTime = System.currentTimeMillis()
        val response = chain.proceed(request)
        val duration = System.currentTimeMillis() - startTime

        // /event 엔드포인트는 기록 제외 (무한 루프 방지)
        val path = request.url.encodedPath
        if (!path.contains("event")) {
            listener?.onHttpEvent(
                method = request.method,
                path = path,
                statusCode = response.code,
                durationMs = duration
            )
        }

        return response
    }
}
