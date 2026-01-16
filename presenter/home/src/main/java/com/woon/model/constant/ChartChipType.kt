package com.woon.model.constant

enum class ChartChipType(
    val label: String,
    val enabled: Boolean = true
) {
    EXCHANGE("거래소"),
    NFT("NFT", enabled = false);
}