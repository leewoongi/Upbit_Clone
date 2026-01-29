package com.woon.model.uimodel

import com.woon.model.constant.SortOrder
import com.woon.model.constant.SortType

data class SortUiState(
    val type: SortType? = SortType.VOLUME,
    val order: SortOrder = SortOrder.DESC
) {
    fun toggle(clickedType: SortType): SortUiState {
        // 다른 컬럼 클릭 → 새 컬럼 DESC
        if (type != clickedType) {
            return SortUiState(clickedType, SortOrder.DESC)
        }

        // 같은 컬럼 클릭 → DESC → ASC → 해제
        return when (order) {
            SortOrder.DESC -> copy(order = SortOrder.ASC)
            SortOrder.ASC -> SortUiState(type = null, order = SortOrder.NONE)
            SortOrder.NONE -> copy(order = SortOrder.DESC)
        }
    }

    fun isSelected(sortType: SortType): Boolean = type == sortType

    fun orderFor(sortType: SortType): SortOrder? =
        if (type == sortType) order else null
}