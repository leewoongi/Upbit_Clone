package com.woon.network.interceptor

interface HttpEventListener {
    fun onHttpEvent(method: String, path: String, statusCode: Int, durationMs: Long)
}
