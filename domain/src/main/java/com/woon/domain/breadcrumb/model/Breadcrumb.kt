package com.woon.domain.breadcrumb.model

data class Breadcrumb(
    val ts: Long,
    val type: BreadcrumbType,
    val name: String,
    val attrs: Map<String, String> = emptyMap(),
    val sessionId: String
)
