package com.woon.network.interceptor

interface HttpEventListener {
    fun onHttpEvent(method: String, path: String, statusCode: Int, durationMs: Long)

    fun onHttpError(
        method: String,
        path: String,
        exceptionType: String,
        message: String,
        durationMs: Long,
        retryCount: Int
    )
}
