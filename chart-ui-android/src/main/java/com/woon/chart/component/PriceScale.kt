package com.woon.chart.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.woon.chart.ui.TradingChartState
import com.woon.chart.ui.extension.formatPrice
import com.woon.chart.ui.theme.chartBackgroundColor
import com.woon.chart.ui.theme.chartTextColor

@Composable
fun PriceScale(
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
            .padding(top = 10.dp, bottom = 10.dp)
    ) {
        if (state.priceRange <= 0.0) return@Canvas

        val textStyle = TextStyle(color = textColor, fontSize = 10.sp)
        val lineCount = 5

        val interval = state.priceRange / lineCount

        for (i in 0..lineCount) {
            val price = state.minPrice + interval * i
            val y = state.priceToY(price)
            val text = price.formatPrice()
            val textLayout = textMeasurer.measure(text, textStyle)
            drawText(
                textLayoutResult = textLayout,
                topLeft = Offset(8f, y - textLayout.size.height / 2)
            )
        }
    }
}