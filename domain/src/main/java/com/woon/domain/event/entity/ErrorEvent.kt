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
    val buildFingerprint: String = "",  // gitSha, versionCode 등 빌드 식별자
    val deviceModel: String = "",
    val osSdkInt: Int = 0,
    val locale: String = "",
    val installId: String = "",  // 설치 단위 고유 ID (세션과 다름)

    // Context 정보
    val thread: String = "",
    val isMainThread: Boolean = false,
    val feature: String = "",
    val flow: String = "",

    // 에러 직전 상태 (서버 정확도 상승용)
    val errorContext: Map<String, String> = emptyMap(),

    // Network 정보
    val networkType: String = "",  // wifi, mobile, offline
    val isAirplaneMode: Boolean = false,
    val networkContext: Map<String, String> = emptyMap(),  // 네트워크 진단 힌트

    // Hint 필드 (서버가 최종 결정, Android는 힌트만 제공)
    val exceptionClass: String = "",
    val topFrameHint: String = "",  // 서버 규칙: topFrameHint > stack parse
    val messageNormalizedHint: String = "",
    val breadcrumbsSummaryHint: String = "",
    val fingerprintHint: String = ""
)
