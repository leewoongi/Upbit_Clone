package com.woon.chart.core.model.indicator.ma.extension

import com.woon.chart.core.model.indicator.ma.MA

/** 가격 범위 계산 (visible 영역) */
fun List<MA>.getPriceRange(
    startIndex: Int,
    count: Int
): Pair<Double, Double>? {
    val visiblePoints = this
        .drop(startIndex)
        .take(count)
        .filter { it.average > 0.0 }

    if (visiblePoints.isEmpty()) return null

    return Pair(
        visiblePoints.minOf { it.average },
        visiblePoints.maxOf { it.average }
    )
}