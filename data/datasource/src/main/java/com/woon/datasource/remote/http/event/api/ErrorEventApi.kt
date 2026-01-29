package com.woon.datasource.remote.http.event.api

import com.woon.datasource.remote.http.event.request.ErrorEventRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ErrorEventApi {
    @POST("event")
    suspend fun sendEvent(@Body request: ErrorEventRequest)
}
