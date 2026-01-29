package com.woon.chart.core.model.indicator.bollingerband

/**
 * 볼린저 밴드를 화면에 그리기 위한 가격
 *
 * @param upper 상단 밴드 Y 가격
 * @param middle 중간 밴드 (SMA) Y 가격
 * @param lower 하단 밴드 Y 가격
 */
data class BollingerBand(
    val upper: Double,
    val middle: Double,
    val lower: Double,
)