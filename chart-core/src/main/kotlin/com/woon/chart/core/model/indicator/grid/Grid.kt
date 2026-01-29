package com.woon.chart.core.model.indicator.grid

/**
 * 차트 그리드 설정
 *
 * 가격/시간 눈금선의 개수를 정의한다.
 *
 * @property lineCount 수평 그리드 라인 개수 (가격 눈금 구간 수)
 */
data class Grid(
    val lineCount: Int = 5
)