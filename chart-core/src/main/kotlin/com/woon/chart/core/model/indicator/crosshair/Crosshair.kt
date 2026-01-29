package com.woon.chart.core.model.indicator.crosshair

/**
 * 십자선 상태
 *
 * 사용자 터치 위치를 표시하는 십자선의 상태와 위치를 담는다.
 * 색상, 스타일 등 UI 속성은 chart-ui-android에서 처리한다.
 *
 * @property enabled 십자선 표시 여부
 * @property x 십자선의 X 좌표 (화면 픽셀 단위)
 * @property y 십자선의 Y 좌표 (화면 픽셀 단위)
 */
data class Crosshair(
    val enabled: Boolean = false,
    val x: Float = 0f,
    val y: Float = 0f
)