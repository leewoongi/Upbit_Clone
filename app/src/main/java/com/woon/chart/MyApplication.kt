package com.woon.chart

import android.app.Application
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

    override fun onCreate() {
        super.onCreate()
        setupHttpBreadcrumb()
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
        })
    }
}
