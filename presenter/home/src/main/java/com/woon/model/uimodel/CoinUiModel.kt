package com.woon.model.uimodel

import androidx.compose.ui.graphics.Color
import com.woon.domain.ticker.entity.constant.ChangeType

data class CoinUiModel(
    val id: String,
    val name: String,           // 오프렛저
    val symbol: String,         // OPEN/KRW
    val price: String,          // 243
    val changeRate: String,     // 4.29%
    val changeType: ChangeType, // RISE, FALL, EVEN
    val volume: String,         // 1,044백만
    // 정렬용 원시값
    val rawPrice: Double,
    val rawChangeRate: Double,
    val rawVolume: Double
)