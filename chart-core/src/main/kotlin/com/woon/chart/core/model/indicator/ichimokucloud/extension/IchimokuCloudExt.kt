package com.woon.chart.core.model.indicator.ichimokucloud.extension

import com.woon.chart.core.model.indicator.ichimokucloud.IchimokuCloud

/** 가격 범위 계산 (visible 영역) */
fun List<IchimokuCloud>.getPriceRange(
    startIndex: Int,
    count: Int,
    displacement: Int = 26
): Pair<Double, Double>? {
    if (isEmpty()) return null

    val prices = mutableListOf<Double>()

    // 1. 전환선, 기준선 (현재 위치에 그려짐)
    for (i in startIndex until (startIndex + count).coerceAtMost(size)) {
        val ichimoku = this[i]
        if (ichimoku.tenkanSen > 0) prices.add(ichimoku.tenkanSen)
        if (ichimoku.kijunSen > 0) prices.add(ichimoku.kijunSen)
    }

    // 2. 선행스팬 (i 인덱스 데이터가 i + displacement 위치에 그려짐)
    // 화면에 보이려면: i + displacement가 startIndex ~ startIndex+count 범위
    // 따라서: i는 startIndex - displacement ~ startIndex + count - displacement
    val cloudStart = (startIndex - displacement).coerceAtLeast(0)
    val cloudEnd = (startIndex + count - displacement).coerceAtMost(size)

    for (i in cloudStart until cloudEnd) {
        val ichimoku = this[i]
        if (ichimoku.senkouSpanA > 0) prices.add(ichimoku.senkouSpanA)
        if (ichimoku.senkouSpanB > 0) prices.add(ichimoku.senkouSpanB)
    }

    // 3. 후행스팬 (i 인덱스 데이터가 i - displacement 위치에 그려짐)
    // 화면에 보이려면: i - displacement가 startIndex ~ startIndex+count 범위
    // 따라서: i는 startIndex + displacement ~ startIndex + count + displacement
    val chikouStart = (startIndex + displacement).coerceAtMost(size)
    val chikouEnd = (startIndex + count + displacement).coerceAtMost(size)

    for (i in chikouStart until chikouEnd) {
        val ichimoku = this[i]
        if (ichimoku.chikouSpan > 0) prices.add(ichimoku.chikouSpan)
    }

    if (prices.isEmpty()) return null

    return Pair(prices.min(), prices.max())
}