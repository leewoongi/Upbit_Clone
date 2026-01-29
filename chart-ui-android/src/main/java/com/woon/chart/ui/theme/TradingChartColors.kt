package com.woon.chart.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * 트레이딩 차트 색상
 */
@Immutable
class TradingChartColors(
    // 캔들 색상
    val rising: Color,
    val falling: Color,

    // 기본 UI 색상
    val grid: Color,
    val text: Color,
    val background: Color,

    // 볼린저 밴드 색상
    val bollingerUpper: Color,
    val bollingerMiddle: Color,
    val bollingerLower: Color,

    // crossHair 색상
    val crossHair: Color,

    // 이동평균선(MA) 색상
    val ma5: Color,
    val ma10: Color,
    val ma20: Color,
    val ma60: Color,
    val ma120: Color,
    val ma200: Color,

    // 일목균형표 색상
    val ichimokuTenkan: Color,
    val ichimokuKijun: Color,
    val ichimokuSenkouA: Color,
    val ichimokuSenkouB: Color,
    val ichimokuChikou: Color,
    val ichimokuCloudBullish: Color,
    val ichimokuCloudBearish: Color,
) {
    fun copy(
        rising: Color = this.rising,
        falling: Color = this.falling,
        grid: Color = this.grid,
        text: Color = this.text,
        background: Color = this.background,
        bollingerUpper: Color = this.bollingerUpper,
        bollingerMiddle: Color = this.bollingerMiddle,
        bollingerLower: Color = this.bollingerLower,
        crossHair: Color = this.crossHair,
        ma5: Color = this.ma5,
        ma10: Color = this.ma10,
        ma20: Color = this.ma20,
        ma60: Color = this.ma60,
        ma120: Color = this.ma120,
        ma200: Color = this.ma200,
        ichimokuTenkan: Color = this.ichimokuTenkan,
        ichimokuKijun: Color = this.ichimokuKijun,
        ichimokuSenkouA: Color = this.ichimokuSenkouA,
        ichimokuSenkouB: Color = this.ichimokuSenkouB,
        ichimokuChikou: Color = this.ichimokuChikou,
        ichimokuCloudBullish: Color = this.ichimokuCloudBullish,
        ichimokuCloudBearish: Color = this.ichimokuCloudBearish,
    ): TradingChartColors = TradingChartColors(
        rising = rising,
        falling = falling,
        grid = grid,
        text = text,
        background = background,
        bollingerUpper = bollingerUpper,
        bollingerMiddle = bollingerMiddle,
        bollingerLower = bollingerLower,
        crossHair = crossHair,
        ma5 = ma5,
        ma10 = ma10,
        ma20 = ma20,
        ma60 = ma60,
        ma120 = ma120,
        ma200 = ma200,
        ichimokuTenkan = ichimokuTenkan,
        ichimokuKijun = ichimokuKijun,
        ichimokuSenkouA = ichimokuSenkouA,
        ichimokuSenkouB = ichimokuSenkouB,
        ichimokuChikou = ichimokuChikou,
        ichimokuCloudBullish = ichimokuCloudBullish,
        ichimokuCloudBearish = ichimokuCloudBearish,
    )
}

/**
 * 다크 테마 색상
 */
fun darkTradingChartColors(): TradingChartColors = TradingChartColors(
    rising = Color(0xFFD24F45),
    falling = Color(0xFF1261C4),
    grid = Color(0xFF2A2A2A),
    text = Color(0xFFAAAAAA),
    background = Color(0xFF000000),
    bollingerUpper = Color(0xFF2196F3),
    bollingerMiddle = Color(0xFFFFA726),
    bollingerLower = Color(0xFF2196F3),
    crossHair = Color(0xFFFFFFFF),
    ma5 = Color(0xFFFF0000),        // Red
    ma10 = Color(0xFFFFEB3B),       // Yellow
    ma20 = Color(0xFF4CAF50),       // Green
    ma60 = Color(0xFF2196F3),       // Blue
    ma120 = Color(0xFFE91E63),      // Magenta
    ma200 = Color(0xFF00BCD4),      // Cyan
    ichimokuTenkan = Color(0xFFE91E63),
    ichimokuKijun = Color(0xFF2196F3),
    ichimokuSenkouA = Color(0xFF4CAF50),
    ichimokuSenkouB = Color(0xFFFF5722),
    ichimokuChikou = Color(0xFF9C27B0),
    ichimokuCloudBullish = Color(0x4D4CAF50),
    ichimokuCloudBearish = Color(0x4DFF5722),
)

/**
 * 라이트 테마 색상
 */
fun lightTradingChartColors(): TradingChartColors = TradingChartColors(
    rising = Color(0xFFE53935),
    falling = Color(0xFF1E88E5),
    grid = Color(0xFFE0E0E0),
    text = Color(0xFF424242),
    background = Color(0xFFFFFFFF),
    bollingerUpper = Color(0xFF42A5F5),
    bollingerMiddle = Color(0xFFFFB74D),
    bollingerLower = Color(0xFF42A5F5),
    crossHair = Color(0xFF000000),
    ma5 = Color(0xFFE53935),        // Red
    ma10 = Color(0xFFFDD835),       // Yellow
    ma20 = Color(0xFF43A047),       // Green
    ma60 = Color(0xFF1E88E5),       // Blue
    ma120 = Color(0xFFD81B60),      // Magenta
    ma200 = Color(0xFF00ACC1),      // Cyan
    ichimokuTenkan = Color(0xFFD81B60),
    ichimokuKijun = Color(0xFF1976D2),
    ichimokuSenkouA = Color(0xFF388E3C),
    ichimokuSenkouB = Color(0xFFE64A19),
    ichimokuChikou = Color(0xFF7B1FA2),
    ichimokuCloudBullish = Color(0x4D4CAF50),
    ichimokuCloudBearish = Color(0x4DFF5722),
)

internal val LocalTradingChartColors = staticCompositionLocalOf { lightTradingChartColors() }