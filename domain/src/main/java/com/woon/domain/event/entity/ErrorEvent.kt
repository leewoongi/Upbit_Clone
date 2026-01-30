package com.woon.domain.event.entity

import com.woon.domain.breadcrumb.model.Breadcrumb

data class ErrorEvent(
    val id: Long = 0L,  // Room 저장용 ID
    val timestamp: Long = System.currentTimeMillis(),
    val type: String = "ERROR",
    val message: String,
    val stack: String = "",
    val screen: String = "",
    val sessionId: String = "",
    val breadcrumbs: List<Breadcrumb> = emptyList(),

    // App/Device 정보
    val appVersion: String = "",
    val buildType: String = "",
    val deviceModel: String = "",
    val osSdkInt: Int = 0,
    val locale: String = "",
    val installId: String = "",  // 익명 디바이스 ID

    // Context 정보
    val thread: String = "",
    val isMainThread: Boolean = false,
    val feature: String = "",
    val flow: String = "",

    // Network 정보
    val networkType: String = "",  // wifi, mobile, offline
    val isAirplaneMode: Boolean = false,

    // LLM Hint 필드
    val exceptionClass: String = "",
    val topFrameHint: String = "",
    val messageNormalizedHint: String = "",
    val breadcrumbsSummaryHint: String = "",
    val fingerprintHint: String = ""
)
