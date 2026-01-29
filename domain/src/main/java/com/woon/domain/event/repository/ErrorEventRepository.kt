package com.woon.domain.event.repository

import com.woon.domain.event.entity.ErrorEvent

interface ErrorEventRepository {
    suspend fun sendEvent(event: ErrorEvent): Result<Unit>
}
