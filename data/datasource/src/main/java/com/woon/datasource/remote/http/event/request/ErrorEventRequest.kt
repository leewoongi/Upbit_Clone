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
    @SerializedName("appVersion") val appVersion: String,
    @SerializedName("buildType") val buildType: String,
    @SerializedName("deviceModel") val deviceModel: String,
    @SerializedName("osSdkInt") val osSdkInt: Int,
    @SerializedName("thread") val thread: String,
    @SerializedName("feature") val feature: String,
    @SerializedName("flow") val flow: String,
    @SerializedName("fingerprintHint") val fingerprintHint: String
)
