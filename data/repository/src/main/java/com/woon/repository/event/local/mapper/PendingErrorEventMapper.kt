package com.woon.repository.event.local.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.woon.datasource.local.room.entity.PendingErrorEventEntity
import com.woon.domain.breadcrumb.model.Breadcrumb
import com.woon.domain.breadcrumb.model.BreadcrumbType
import com.woon.domain.event.entity.ErrorEvent

private val gson = Gson()

fun ErrorEvent.toEntity(): PendingErrorEventEntity {
    return PendingErrorEventEntity(
        timestamp = timestamp,
        type = type,
        message = message,
        stack = stack,
        screen = screen,
        sessionId = sessionId,
        breadcrumbsJson = gson.toJson(breadcrumbs.map { it.toJsonModel() }),

        // App/Device 정보
        appVersion = appVersion,
        buildType = buildType,
        buildFingerprint = buildFingerprint,
        deviceModel = deviceModel,
        osSdkInt = osSdkInt,
        locale = locale,
        installId = installId,

        // Context 정보
        thread = thread,
        isMainThread = isMainThread,
        feature = feature,
        flow = flow,

        // 에러 직전 상태
        errorContextJson = gson.toJson(errorContext),

        // Network 정보
        networkType = networkType,
        isAirplaneMode = isAirplaneMode,
        networkContextJson = gson.toJson(networkContext),

        // LLM Hint 필드
        exceptionClass = exceptionClass,
        topFrameHint = topFrameHint,
        messageNormalizedHint = messageNormalizedHint,
        breadcrumbsSummaryHint = breadcrumbsSummaryHint,
        fingerprintHint = fingerprintHint
    )
}

fun PendingErrorEventEntity.toDomain(): ErrorEvent {
    val breadcrumbType = object : TypeToken<List<BreadcrumbJsonModel>>() {}.type
    val breadcrumbJsonList: List<BreadcrumbJsonModel> = gson.fromJson(breadcrumbsJson, breadcrumbType)

    val mapType = object : TypeToken<Map<String, String>>() {}.type
    val errorContext: Map<String, String> = runCatching {
        gson.fromJson<Map<String, String>>(errorContextJson, mapType)
    }.getOrDefault(emptyMap())
    val networkContext: Map<String, String> = runCatching {
        gson.fromJson<Map<String, String>>(networkContextJson, mapType)
    }.getOrDefault(emptyMap())

    return ErrorEvent(
        id = id,
        timestamp = timestamp,
        type = type,
        message = message,
        stack = stack,
        screen = screen,
        sessionId = sessionId,
        breadcrumbs = breadcrumbJsonList.map { it.toBreadcrumb() },

        // App/Device 정보
        appVersion = appVersion,
        buildType = buildType,
        buildFingerprint = buildFingerprint,
        deviceModel = deviceModel,
        osSdkInt = osSdkInt,
        locale = locale,
        installId = installId,

        // Context 정보
        thread = thread,
        isMainThread = isMainThread,
        feature = feature,
        flow = flow,

        // 에러 직전 상태
        errorContext = errorContext,

        // Network 정보
        networkType = networkType,
        isAirplaneMode = isAirplaneMode,
        networkContext = networkContext,

        // LLM Hint 필드
        exceptionClass = exceptionClass,
        topFrameHint = topFrameHint,
        messageNormalizedHint = messageNormalizedHint,
        breadcrumbsSummaryHint = breadcrumbsSummaryHint,
        fingerprintHint = fingerprintHint
    )
}

// JSON 직렬화용 모델
private data class BreadcrumbJsonModel(
    val ts: Long,
    val type: String,
    val name: String,
    val attrs: Map<String, String>,
    val sessionId: String
)

private fun Breadcrumb.toJsonModel(): BreadcrumbJsonModel {
    return BreadcrumbJsonModel(
        ts = ts,
        type = type.name,
        name = name,
        attrs = attrs,
        sessionId = sessionId
    )
}

private fun BreadcrumbJsonModel.toBreadcrumb(): Breadcrumb {
    return Breadcrumb(
        ts = ts,
        type = BreadcrumbType.valueOf(type),
        name = name,
        attrs = attrs,
        sessionId = sessionId
    )
}
