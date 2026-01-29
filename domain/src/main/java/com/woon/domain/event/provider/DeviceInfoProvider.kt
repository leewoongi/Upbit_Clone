package com.woon.domain.event.provider

interface DeviceInfoProvider {
    val deviceModel: String  // Build.MODEL
    val osSdkInt: Int        // Build.VERSION.SDK_INT
}
