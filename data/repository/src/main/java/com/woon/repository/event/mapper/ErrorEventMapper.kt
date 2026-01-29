package com.woon.repository.event.mapper

import com.woon.datasource.remote.http.event.request.ErrorEventRequest
import com.woon.domain.event.entity.ErrorEvent

fun ErrorEvent.toRequest(): ErrorEventRequest {
    return ErrorEventRequest(
        timestamp = timestamp,
        type = type,
        message = message,
        stack = stack,
        screen = screen
    )
}
