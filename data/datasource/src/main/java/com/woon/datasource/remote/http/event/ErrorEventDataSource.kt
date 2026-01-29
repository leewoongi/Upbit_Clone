package com.woon.datasource.remote.http.event

import com.woon.datasource.remote.http.event.request.ErrorEventRequest

interface ErrorEventDataSource {
    suspend fun sendEvent(request: ErrorEventRequest)
}
