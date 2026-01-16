package com.woon.core.ui.design.theme.font

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

@Immutable
class ChartTypography(
    // 큰 가격 표시 (128,975,000)
    val displayLarge: TextStyle = ChartTypographyPlatte.DisplayLarge,

    // 중간 가격 표시
    val displayMedium: TextStyle = ChartTypographyPlatte.DisplayMedium,

    // 작은 가격 표시
    val displaySmall: TextStyle = ChartTypographyPlatte.DisplaySmall,

    // 코인 이름 (비트코인, 이더리움)
    val headlineLarge: TextStyle = ChartTypographyPlatte.HeadlineLarge,

    // 섹션 타이틀 (총 매수, 총 평가)
    val headlineMedium: TextStyle = ChartTypographyPlatte.HeadlineMedium,

    // 거래소 이름, 섹션 타이틀 (한글명, 현재가, 전일대비)
    val headlineSmall: TextStyle = ChartTypographyPlatte.HeadlineSmall,

    // 변동률 (-19.07%, -2.14%)
    val titleLarge: TextStyle = ChartTypographyPlatte.TitleLarge,

    // 탭 텍스트 (거래소, NFT)
    val titleMedium: TextStyle = ChartTypographyPlatte.TitleMedium,

    // 마켓 필터 버튼 (KRW, BTC, USDT, 관심)
    val titleSmall: TextStyle = ChartTypographyPlatte.TitleSmall,

    // 코인 현재가 (348, 2,852, 128,975,000)
    val bodyLarge: TextStyle = ChartTypographyPlatte.BodyLarge,

    // 전일대비 금액 (-82, -108, -2,820,000)
    val bodyMedium: TextStyle = ChartTypographyPlatte.BodyMedium,

    // 코인 심볼 (WET/KRW, XRP/KRW, BTC/KRW)
    val bodySmall: TextStyle = ChartTypographyPlatte.BodySmall,

    // 하단 네비게이션 텍스트 (거래소, 코인정보, 투자내역)
    val labelLarge: TextStyle = ChartTypographyPlatte.LabelLarge,

    // 거래대금 (112,433백만, 349,647백만)
    val labelMedium: TextStyle = ChartTypographyPlatte.LabelMedium,

    // 뱃지 텍스트 (N, 주, 밈)
    val labelSmall: TextStyle = ChartTypographyPlatte.LabelSmall,
) {
    fun copy(
        displayLarge: TextStyle = this.displayLarge,
        displayMedium: TextStyle = this.displayMedium,
        displaySmall: TextStyle = this.displaySmall,
        headlineLarge: TextStyle = this.headlineLarge,
        headlineMedium: TextStyle = this.headlineMedium,
        headlineSmall: TextStyle = this.headlineSmall,
        titleLarge: TextStyle = this.titleLarge,
        titleMedium: TextStyle = this.titleMedium,
        titleSmall: TextStyle = this.titleSmall,
        bodyLarge: TextStyle = this.bodyLarge,
        bodyMedium: TextStyle = this.bodyMedium,
        bodySmall: TextStyle = this.bodySmall,
        labelLarge: TextStyle = this.labelLarge,
        labelMedium: TextStyle = this.labelMedium,
        labelSmall: TextStyle = this.labelSmall,
    ): ChartTypography {
        return ChartTypography(
            displayLarge = displayLarge,
            displayMedium = displayMedium,
            displaySmall = displaySmall,
            headlineLarge = headlineLarge,
            headlineMedium = headlineMedium,
            headlineSmall = headlineSmall,
            titleLarge = titleLarge,
            titleMedium = titleMedium,
            titleSmall = titleSmall,
            bodyLarge = bodyLarge,
            bodyMedium = bodyMedium,
            bodySmall = bodySmall,
            labelLarge = labelLarge,
            labelMedium = labelMedium,
            labelSmall = labelSmall,
        )
    }

    override fun toString(): String {
        return "ChartTypography(" +
                "displayLarge=$displayLarge, " +
                "displayMedium=$displayMedium, " +
                "displaySmall=$displaySmall, " +
                "headlineLarge=$headlineLarge, " +
                "headlineMedium=$headlineMedium, " +
                "headlineSmall=$headlineSmall, " +
                "titleLarge=$titleLarge, " +
                "titleMedium=$titleMedium, " +
                "titleSmall=$titleSmall, " +
                "bodyLarge=$bodyLarge, " +
                "bodyMedium=$bodyMedium, " +
                "bodySmall=$bodySmall, " +
                "labelLarge=$labelLarge, " +
                "labelMedium=$labelMedium, " +
                "labelSmall=$labelSmall" +
                ")"
    }
}

val LocalTypography = staticCompositionLocalOf { ChartTypography() }

