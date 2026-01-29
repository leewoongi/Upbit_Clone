package com.woon.chart.core.model.indicator.ichimokucloud

data class IchimokuCloud(
    val tenkanSen: Double,    // 전환선
    val kijunSen: Double,     // 기준선
    val senkouSpanA: Double,  // 선행스팬1
    val senkouSpanB: Double,  // 선행스팬2
    val chikouSpan: Double    // 후행스팬
)