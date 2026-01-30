package com.woon.datasource.remote.http.event.request

import com.google.gson.annotations.SerializedName

data class ErrorEventRequest(
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("type") val type: String,
    @SerializedName("message") val message: String,
    @SerializedName("stack") val stack: String,
    @SerializedName("screen") val screen: String,
    @SerializedName("sessionId") val sessionId: String,
    @SerializedName("breadcrumbs") val breadcrumbs: List<BreadcrumbRequest>,

    // App/Device 정보
    @SerializedName("appVersion") val appVersion: String,
    @SerializedName("buildType") val buildType: String,
    @SerializedName("deviceModel") val deviceModel: String,
    @SerializedName("osSdkInt") val osSdkInt: Int,
    @SerializedName("locale") val locale: String,
    @SerializedName("installId") val installId: String,

    // Context 정보
    @SerializedName("thread") val thread: String,
    @SerializedName("isMainThread") val isMainThread: Boolean,
    @SerializedName("feature") val feature: String,
    @SerializedName("flow") val flow: String,

    // Network 정보
    @SerializedName("networkType") val networkType: String,
    @SerializedName("isAirplaneMode") val isAirplaneMode: Boolean,

    // LLM Hint 필드
    @SerializedName("exceptionClass") val exceptionClass: String,
    @SerializedName("topFrameHint") val topFrameHint: String,
    @SerializedName("messageNormalizedHint") val messageNormalizedHint: String,
    @SerializedName("breadcrumbsSummaryHint") val breadcrumbsSummaryHint: String,
    @SerializedName("fingerprintHint") val fingerprintHint: String
)
