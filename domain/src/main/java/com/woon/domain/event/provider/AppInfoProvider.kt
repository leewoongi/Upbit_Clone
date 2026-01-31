package com.woon.domain.event.provider

interface AppInfoProvider {
    val appVersion: String      // e.g., "1.3.0(45)"
    val buildType: String       // "debug" or "release"
    val locale: String          // e.g., "ko_KR"
    val buildFingerprint: String  // e.g., "1.0.0-debug-abc1234"
}
