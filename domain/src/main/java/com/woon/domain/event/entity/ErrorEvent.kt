package com.woon.domain.event.entity

import com.woon.domain.breadcrumb.model.Breadcrumb

data class ErrorEvent(
    val timestamp: Long = System.currentTimeMillis(),
    val type: String = "ERROR",
    val message: String,
    val stack: String = "",
    val screen: String = "",
    val sessionId: String = "",
    val breadcrumbs: List<Breadcrumb> = emptyList(),
    // 새로 추가된 필드
    val appVersion: String = "",
    val buildType: String = "",
    val deviceModel: String = "",
    val osSdkInt: Int = 0,
    val thread: String = "",
    val feature: String = "",
    val flow: String = "",
    val fingerprintHint: String = ""
)
