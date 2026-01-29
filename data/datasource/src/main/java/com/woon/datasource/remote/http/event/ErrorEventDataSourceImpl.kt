package com.woon.datasource.remote.http.event

import com.woon.datasource.remote.http.event.api.ErrorEventApi
import com.woon.datasource.remote.http.event.request.ErrorEventRequest
import javax.inject.Inject

class ErrorEventDataSourceImpl @Inject constructor(
    private val errorEventApi: ErrorEventApi
) : ErrorEventDataSource {

    override suspend fun sendEvent(request: ErrorEventRequest) {
        errorEventApi.sendEvent(request)
    }
}
