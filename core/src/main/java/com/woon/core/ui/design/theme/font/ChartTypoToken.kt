package com.woon.core.ui.design.theme.font

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

internal object ChartTypographyPlatte {
    // 큰 가격 표시 (128,975,000)
    val DisplayLarge = TextStyle(
        fontFamily = RobotoMono,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        letterSpacing = (-1).sp
    )

    // 중간 가격 표시
    val DisplayMedium = TextStyle(
        fontFamily = RobotoMono,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp
    )

    // 작은 가격 표시
    val DisplaySmall = TextStyle(
        fontFamily = RobotoMono,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    )

    // 코인 이름 (비트코인, 이더리움)
    val HeadlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp
    )

    // 섹션 타이틀 (총 매수, 총 평가)
    val HeadlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    )

    // 거래소 이름, 테이블 헤더 (한글명, 현재가)
    val HeadlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp
    )

    // 변동률 (-19.07%, -2.14%)
    val TitleLarge = TextStyle(
        fontFamily = RobotoMono,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    )

    // 탭 텍스트 (거래소, NFT)
    val TitleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp
    )

    // 마켓 필터 버튼 (KRW, BTC, USDT)
    val TitleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )

    // 코인 현재가 (348, 2,852)
    val BodyLarge = TextStyle(
        fontFamily = RobotoMono,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )

    // 전일대비 금액 (-82, -108)
    val BodyMedium = TextStyle(
        fontFamily = RobotoMono,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )

    // 코인 심볼 (WET/KRW, XRP/KRW)
    val BodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp
    )

    // 하단 네비게이션 (거래소, 코인정보)
    val LabelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    )

    // 거래대금 (112,433백만)
    val LabelMedium = TextStyle(
        fontFamily = RobotoMono,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp
    )

    // 뱃지 텍스트 (N, 주, 밈)
    val LabelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 9.sp
    )
}
