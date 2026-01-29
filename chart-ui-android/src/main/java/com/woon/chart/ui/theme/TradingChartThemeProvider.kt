package com.woon.chart.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun TradingChartThemeProvider(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colors: TradingChartColors = if (darkTheme) darkTradingChartColors() else lightTradingChartColors(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalTradingChartColors provides colors) {
        content()
    }
}