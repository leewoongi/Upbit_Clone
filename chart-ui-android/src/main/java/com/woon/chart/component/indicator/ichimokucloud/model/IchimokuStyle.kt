package com.woon.chart.component.indicator.ichimokucloud.model

import androidx.compose.ui.graphics.Color

data class IchimokuStyle(
    val tenkanColor: Color = Color(0xFFE91E63),      // 빨강 - 전환선
    val kijunColor: Color = Color(0xFF2196F3),       // 파랑 - 기준선
    val senkouSpanAColor: Color = Color(0xFF4CAF50), // 초록 - 선행스팬A
    val senkouSpanBColor: Color = Color(0xFFFF5722), // 주황 - 선행스팬B
    val chikouColor: Color = Color(0xFF9C27B0),      // 보라 - 후행스팬
    val cloudBullishColor: Color = Color(0x4D4CAF50), // 상승 구름 (반투명 초록)
    val cloudBearishColor: Color = Color(0x4DFF5722), // 하락 구름 (반투명 빨강)
    val strokeWidth: Float = 1.5f
)