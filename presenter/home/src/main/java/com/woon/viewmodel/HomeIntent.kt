package com.woon.viewmodel

import com.woon.model.constant.SortType

sealed class HomeIntent {
    data class Sort(val type: SortType) : HomeIntent()
}
