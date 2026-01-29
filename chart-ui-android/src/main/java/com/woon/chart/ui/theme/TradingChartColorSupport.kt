package com.woon.chart.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


// 캔들 색상
val chartRisingColor: Color @Composable get() = TradingChartTheme.colors.rising
val chartFallingColor: Color @Composable get() = TradingChartTheme.colors.falling

// 기본 UI 색상
val chartGridColor: Color @Composable get() = TradingChartTheme.colors.grid
val chartTextColor: Color @Composable get() = TradingChartTheme.colors.text
val chartBackgroundColor: Color @Composable get() = TradingChartTheme.colors.background

// 볼린저 밴드 색상
val chartBollingerUpperColor: Color @Composable get() = TradingChartTheme.colors.bollingerUpper
val chartBollingerMiddleColor: Color @Composable get() = TradingChartTheme.colors.bollingerMiddle
val chartBollingerLowerColor: Color @Composable get() = TradingChartTheme.colors.bollingerLower

// crossHair 색상
val chartCrossHairColor: Color @Composable get() = TradingChartTheme.colors.crossHair

// 이동평균선(MA) 색상
val chartMa5Color: Color @Composable get() = TradingChartTheme.colors.ma5
val chartMa10Color: Color @Composable get() = TradingChartTheme.colors.ma10
val chartMa20Color: Color @Composable get() = TradingChartTheme.colors.ma20
val chartMa60Color: Color @Composable get() = TradingChartTheme.colors.ma60
val chartMa120Color: Color @Composable get() = TradingChartTheme.colors.ma120
val chartMa200Color: Color @Composable get() = TradingChartTheme.colors.ma200

// 일목균형표 색상
val chartIchimokuTenkanColor: Color @Composable get() = TradingChartTheme.colors.ichimokuTenkan
val chartIchimokuKijunColor: Color @Composable get() = TradingChartTheme.colors.ichimokuKijun
val chartIchimokuSenkouAColor: Color @Composable get() = TradingChartTheme.colors.ichimokuSenkouA
val chartIchimokuSenkouBColor: Color @Composable get() = TradingChartTheme.colors.ichimokuSenkouB
val chartIchimokuChikouColor: Color @Composable get() = TradingChartTheme.colors.ichimokuChikou
val chartIchimokuCloudBullishColor: Color @Composable get() = TradingChartTheme.colors.ichimokuCloudBullish
val chartIchimokuCloudBearishColor: Color @Composable get() = TradingChartTheme.colors.ichimokuCloudBearish