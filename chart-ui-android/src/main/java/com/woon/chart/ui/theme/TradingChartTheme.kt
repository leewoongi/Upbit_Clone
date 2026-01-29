package com.woon.chart.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

object TradingChartTheme {
    val colors: TradingChartColors
        @Composable
        @ReadOnlyComposable
        get() = LocalTradingChartColors.current
}