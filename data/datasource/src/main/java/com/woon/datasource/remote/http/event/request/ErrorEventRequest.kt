package com.woon.datasource.remote.http.event.request

import com.google.gson.annotations.SerializedName

data class ErrorEventRequest(
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("type") val type: String,
    @SerializedName("message") val message: String,
    @SerializedName("stack") val stack: String,
    @SerializedName("screen") val screen: String
)
