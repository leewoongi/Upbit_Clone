package com.woon.chart.core.model.indicator.bollingerband.extension

import com.woon.chart.core.model.indicator.bollingerband.BollingerBand

fun List<BollingerBand>.getPriceRange(
    startIndex: Int,
    count: Int
): Pair<Double, Double>? {
    val visiblePoints = this
        .drop(startIndex)
        .take(count)
        .filter { it.lower > 0.0 }

    if (visiblePoints.isEmpty()) return null

    return Pair(
        visiblePoints.minOf { it.lower },
        visiblePoints.maxOf { it.upper }
    )
}