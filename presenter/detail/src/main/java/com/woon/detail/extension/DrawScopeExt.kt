package com.woon.detail.extension

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.woon.detail.model.CandleUiModel

/**
 * @param uiModel 캔들 데이터
 * @param priceRange 가격 범위
 * @param positionX 캔들 중심 X 좌표
 */
internal fun DrawScope.drawCandle(
    uiModel: CandleUiModel,
    priceRange: Pair<Double, Double>,
    positionX: Float,        // 추가: 캔들의 중심 X 좌표
){
    val canvasHeight = size.height

    val highY = priceRange.toScale(uiModel.high) * canvasHeight
    val lowY = priceRange.toScale(uiModel.low) * canvasHeight
    val openY = priceRange.toScale(uiModel.open) * canvasHeight
    val tradeY = priceRange.toScale(uiModel.trade) * canvasHeight

    val bodyTop = minOf(openY, tradeY)
    val bodyBottom = maxOf(openY, tradeY)
    val bodyHeight = (bodyBottom - bodyTop).coerceAtLeast(1f)
    val bodyWidth = uiModel.width * 0.8f


    if(uiModel.isTopWick()) {
        drawLine(
            color = uiModel.getColor(),
            start = Offset(positionX, highY),      // 고가에서 시작
            end = Offset(positionX, bodyTop),      // 몸통 위까지만
            strokeWidth = 2.dp.toPx()
        )
    }

    if (uiModel.isBottomWick()){
        drawLine(
            color = uiModel.getColor(),
            start = Offset(positionX, bodyBottom),  // 몸통 아래에서 시작
            end = Offset(positionX, lowY),          // 저가에서 끝
            strokeWidth = 2.dp.toPx()
        )
    }

    drawRect(
        color = uiModel.getColor(),
        topLeft = Offset(positionX - bodyWidth / 2f, bodyTop),
        size = Size(bodyWidth, bodyHeight),
    )
}


internal fun DrawScope.drawChartBackground(
    candleCount: Int,
    candleWidth: Float,
    candleSpacing: Float,
    priceRange: Pair<Double, Double>
) {
    // 세로 격자선 (시간축)
    val candleSpaceWidth = candleWidth + candleSpacing
    val verticalLineCount = candleCount

    for (i in 0 until verticalLineCount step 5) { // 5개마다 격자선
        val x = i * candleSpaceWidth
        drawLine(
            color = Color.Gray.copy(alpha = 0.2f),
            start = Offset(x, 0f),
            end = Offset(x, size.height),
            strokeWidth = 1.dp.toPx()
        )
    }

    // 가로 격자선 (가격축)
    val horizontalLineCount = 6
    repeat(horizontalLineCount) { i ->
        val ratio = i.toFloat() / (horizontalLineCount - 1)
        val y = size.height * ratio

        drawLine(
            color = Color.Gray.copy(alpha = 0.2f),
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = 1.dp.toPx()
        )
    }
}