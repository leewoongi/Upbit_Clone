package com.woon.detail.extension

// 캔버스의 높이에 따라 가격을 Y좌표로 변환하는 함수
// 리턴된 값에 canvasHeight 곱해야 함
/**
 * (0, 0) ←─ 화면 맨 위 (최고가 위치)
 *   │
 *   │  100픽셀 ←─ 145,100,000원 위치
 *   │
 *   ▼
 * (0, canvasHeight) ←─ 화면 맨 아래 (최저가 위치)
 */
internal fun Pair<Double, Double>.toScale(
    price: Double
): Float {
    val high = this.first
    val low = this.second

    val ratio = (high - price) / (high - low)
    return ratio.toFloat()
}