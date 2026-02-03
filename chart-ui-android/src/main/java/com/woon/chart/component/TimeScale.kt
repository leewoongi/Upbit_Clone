package com.woon.chart.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.woon.chart.ui.TradingChartState
import com.woon.chart.ui.extension.toTimeFormat
import com.woon.chart.ui.theme.chartBackgroundColor
import com.woon.chart.ui.theme.chartTextColor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TimeScale(
    state: TradingChartState,
    modifier: Modifier = Modifier,
    textColor: Color = chartTextColor,
    backgroundColor: Color = chartBackgroundColor
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        val firstCandle = state.visibleCandles.firstOrNull() ?: return@Canvas
        val lastCandle = state.visibleCandles.lastOrNull() ?: return@Canvas
        if (state.visibleCandles.size < 2) return@Canvas

        val textStyle = TextStyle(
            color = textColor,
            fontSize = 10.sp
        )

        // 1. 필요한 정보
        val startTime = firstCandle.timestamp
        val endTime = lastCandle.timestamp

        // 2. 간격 결정
        val interval = state.gridIntervalMs
        val formatter = SimpleDateFormat(interval.toTimeFormat(), Locale.getDefault())

        // 3. 첫 번째 레이블 시간
        val firstTime = ((startTime / interval) + 1) * interval

        // 4. 레이블 그리기
        var time = firstTime
        while (time <= endTime) {
            val x = state.timestampToScreenX(time)

            if (x in 0f..state.screenWidth) {
                val text = formatter.format(Date(time))
                val textLayout = textMeasurer.measure(text, textStyle)

                drawText(
                    textLayoutResult = textLayout,
                    topLeft = Offset(
                        x = x - textLayout.size.width / 2,
                        y = (size.height - textLayout.size.height) / 2
                    )
                )
            }
            time += interval
        }
    }
}