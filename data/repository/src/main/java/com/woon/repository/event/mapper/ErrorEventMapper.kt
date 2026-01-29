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
        breadcrumbs = breadcrumbs.map { it.toRequest() },
        appVersion = appVersion,
        buildType = buildType,
        deviceModel = deviceModel,
        osSdkInt = osSdkInt,
        thread = thread,
        feature = feature,
        flow = flow,
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
