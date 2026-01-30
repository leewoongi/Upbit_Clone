package com.woon.datasource.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_error_events")
data class PendingErrorEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long,
    val type: String,
    val message: String,
    val stack: String,
    val screen: String,
    val sessionId: String,
    val breadcrumbsJson: String,

    // App/Device 정보
    val appVersion: String,
    val buildType: String,
    val deviceModel: String,
    val osSdkInt: Int,
    val locale: String = "",
    val installId: String = "",

    // Context 정보
    val thread: String,
    val isMainThread: Boolean = false,
    val feature: String,
    val flow: String,

    // Network 정보
    val networkType: String = "",
    val isAirplaneMode: Boolean = false,

    // LLM Hint 필드
    val exceptionClass: String = "",
    val topFrameHint: String = "",
    val messageNormalizedHint: String = "",
    val breadcrumbsSummaryHint: String = "",
    val fingerprintHint: String,

    val createdAt: Long = System.currentTimeMillis()
)
