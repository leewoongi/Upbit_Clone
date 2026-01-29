package com.woon.domain.event.provider

interface AppInfoProvider {
    val appVersion: String   // e.g., "1.3.0(45)"
    val buildType: String    // "debug" or "release"
}
