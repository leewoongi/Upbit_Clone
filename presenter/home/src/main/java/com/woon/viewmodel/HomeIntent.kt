package com.woon.viewmodel

import com.woon.model.constant.SortType

sealed class HomeIntent {
    data class Sort(val type: SortType) : HomeIntent()
    data object NotificationClick : HomeIntent()

    // 테스트용 Intent (Debug only)
    data object TestCriticalError : HomeIntent()
    data object TestDomainError : HomeIntent()
}
