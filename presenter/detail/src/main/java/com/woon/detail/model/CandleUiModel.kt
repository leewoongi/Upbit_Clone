package com.woon.detail.model

import androidx.compose.ui.graphics.Color

/**
 * @param high 고가
 * @param low 저가
 * @param open 시가
 * @param trade 종가 (현재가)
 */
data class CandleUiModel(
    val high: Double,
    val low: Double,
    val open: Double,
    val trade: Double,
    val width: Float = 20f
) {
    val isBullish: Boolean get() = trade >= open

    fun getColor(): Color {
        return if (isBullish) Color.Green else Color.Red
    }

    // 상단 꼬리 확인
    fun isTopWick(): Boolean {
        return trade < high
    }

    // 하단 꼬리 확인
    fun isBottomWick(): Boolean {
        return trade > low
    }

    // 캔들이 그려질 위치
    fun getPositionX(
        index: Int,
        candleSpacing: Float = 4f
    ): Float {
        return index * (width + candleSpacing) + width / 2f
    }
}