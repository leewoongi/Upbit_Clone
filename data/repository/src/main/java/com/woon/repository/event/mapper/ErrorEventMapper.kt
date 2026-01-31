package com.woon.repository.event.mapper

import com.woon.datasource.remote.http.event.request.BreadcrumbRequest
import com.woon.datasource.remote.http.event.request.ErrorEventRequest
import com.woon.domain.breadcrumb.model.Breadcrumb
import com.woon.domain.event.entity.ErrorEvent

fun ErrorEvent.toRequest(): ErrorEventRequest {
    return ErrorEventRequest(
        timestamp = timestamp,
        type = type,
        message = message,
        stack = stack,
        screen = screen,
        sessionId = sessionId,
        // breadcrumbs는 ts 오름차순으로 정렬해서 전송
        breadcrumbs = breadcrumbs.sortedBy { it.ts }.map { it.toRequest() },

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

        // Hint 필드 (서버가 최종 결정)
        exceptionClass = exceptionClass,
        topFrameHint = topFrameHint,
        messageNormalizedHint = messageNormalizedHint,
        breadcrumbsSummaryHint = breadcrumbsSummaryHint,
        fingerprintHint = fingerprintHint
    )
}

fun Breadcrumb.toRequest(): BreadcrumbRequest {
    return BreadcrumbRequest(
        ts = ts,
        type = type.name,
        name = name,
        attrs = attrs,
        sessionId = sessionId
    )
}
