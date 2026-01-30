package com.woon.domain.breadcrumb.model

enum class BreadcrumbType {
    SCREEN,
    CLICK,
    NAV,
    HTTP,
    SYSTEM  // 비행기 모드, WiFi, 네트워크 변경 등 시스템 이벤트
}
