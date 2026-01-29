package com.woon.chart.ui.extension

import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

/** 가격 격자 간격 */
fun Double.toPriceInterval(): Double {
    val ideal = this / 5
    val mag = 10.0.pow(floor(log10(ideal)))
    val norm = ideal / mag

    val mul = when {
        norm <= 1.5 -> 1.0
        norm <= 3.5 -> 2.0
        norm <= 7.5 -> 5.0
        else -> 10.0
    }
    return mul * mag
}

/** 가격 포맷 */
fun Double.formatPrice(): String {
    return when {
        this >= 1_000_000 -> "%,.0f".format(this)
        this >= 1_000 -> "%,.0f".format(this)
        this >= 1 -> "%.2f".format(this)
        else -> "%.4f".format(this)
    }
}