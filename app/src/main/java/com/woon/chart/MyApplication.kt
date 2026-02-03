package com.woon.chart

import android.app.Application
import com.woon.chart.receiver.SystemEventReceiver
import com.woon.domain.breadcrumb.recorder.BreadcrumbRecorder
import com.woon.network.interceptor.HttpEventInterceptor
import com.woon.network.interceptor.HttpEventListener
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {

    @Inject
    lateinit var httpEventInterceptor: HttpEventInterceptor

    @Inject
    lateinit var breadcrumbRecorder: BreadcrumbRecorder

    @Inject
    lateinit var systemEventReceiver: SystemEventReceiver

    override fun onCreate() {
        super.onCreate()
        setupHttpBreadcrumb()
        setupSystemEventReceiver()
    }

    private fun setupHttpBreadcrumb() {
        httpEventInterceptor.setListener(object : HttpEventListener {
            override fun onHttpEvent(method: String, path: String, statusCode: Int, durationMs: Long) {
                breadcrumbRecorder.recordHttp(
                    method = method,
                    path = path,
                    statusCode = statusCode,
                    attrs = mapOf("duration" to "${durationMs}ms")
                )
            }

            override fun onHttpError(
                method: String,
                path: String,
                exceptionType: String,
                message: String,
                durationMs: Long,
                retryCount: Int
            ) {
                // HTTP 에러를 breadcrumb으로 기록
                breadcrumbRecorder.recordHttp(
                    method = method,
                    path = path,
                    statusCode = -1,  // 에러를 나타내는 특수값
                    attrs = mapOf(
                        "error" to exceptionType,
                        "message" to message.take(50),
                        "duration" to "${durationMs}ms",
                        "retry" to retryCount.toString()
                    )
                )
            }
        })
    }

    private fun setupSystemEventReceiver() {
        systemEventReceiver.register(this)
    }
}
