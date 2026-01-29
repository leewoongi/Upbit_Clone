package com.woon.domain.event.entity

data class ErrorEvent(
    val timestamp: Long = System.currentTimeMillis(),
    val type: String = "ERROR",
    val message: String,
    val stack: String = "",
    val screen: String = ""
)
