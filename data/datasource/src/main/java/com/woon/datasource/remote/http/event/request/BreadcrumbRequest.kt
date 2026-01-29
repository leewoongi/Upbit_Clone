package com.woon.datasource.remote.http.event.request

import com.google.gson.annotations.SerializedName

data class BreadcrumbRequest(
    @SerializedName("ts") val ts: Long,
    @SerializedName("type") val type: String,
    @SerializedName("name") val name: String,
    @SerializedName("attrs") val attrs: Map<String, String>,
    @SerializedName("sessionId") val sessionId: String
)
