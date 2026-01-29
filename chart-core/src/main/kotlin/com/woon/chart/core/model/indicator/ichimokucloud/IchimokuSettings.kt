package com.woon.chart.core.model.indicator.ichimokucloud

data class IchimokuSettings(
    val enabled: Boolean = false,
    val tenkanPeriod: Int = 9,
    val kijunPeriod: Int = 26,
    val senkouBPeriod: Int = 52,
    val displacement: Int = 26
)