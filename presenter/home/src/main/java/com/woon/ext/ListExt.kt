package com.woon.ext

import com.woon.model.constant.SortOrder
import com.woon.model.constant.SortType
import com.woon.model.uimodel.CoinUiModel
import com.woon.model.uimodel.SortUiState

internal fun List<CoinUiModel>.sortedBy(
    sort: SortUiState
): List<CoinUiModel> {
    val sortType = sort.type ?: return this

    val comparator = when (sortType) {
        SortType.NAME -> compareBy<CoinUiModel> { it.name }
        SortType.PRICE -> compareBy { it.rawPrice }
        SortType.CHANGE -> compareBy { it.rawChangeRate }
        SortType.VOLUME -> compareBy { it.rawVolume }
    }

    return when (sort.order) {
        SortOrder.DESC -> sortedWith(comparator.reversed())
        SortOrder.ASC -> sortedWith(comparator)
        SortOrder.NONE -> this
    }
}