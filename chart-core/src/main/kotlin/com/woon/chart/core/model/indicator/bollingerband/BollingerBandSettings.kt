package com.woon.chart.core.model.indicator.bollingerband

/**
 * 볼린저 밴드 계산 설정
 *
 * 볼린저 밴드 지표 계산에 필요한 파라미터를 정의한다.
 *
 * @property enabled 볼린저 밴드 표시 여부
 * @property period 이동평균 기간 (N일). 기본값 20
 * @property multiplier 표준편차 배수 (k). 상단/하단 밴드 = SMA ± k × σ. 기본값 2
 */
data class BollingerBandSettings(
    val enabled: Boolean = false,
    val period: Int = 20,
    val multiplier: Float = 2f
)
