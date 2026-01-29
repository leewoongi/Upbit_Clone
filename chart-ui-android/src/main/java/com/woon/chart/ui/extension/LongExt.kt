package com.woon.chart.ui.extension

import kotlin.math.abs

/** 시간 포맷 */
fun Long.toTimeFormat(): String {
    return when {
        this < 24 * 60 * 60_000L -> "HH:mm"
        else -> "MM/dd"
    }
}

/** 깔끔한 시간 단위로 반올림 */
fun Long.toCleanTimeInterval(): Long {
    val minutes = this / 60_000

    if (minutes < 60) {
        val cleanMinutes = listOf(1, 5, 10, 15, 30)
        val best = cleanMinutes.minByOrNull { abs(it - minutes) } ?: 5
        return best * 60_000L
    }

    val hours = this / (60 * 60_000)

    if (hours < 24) {
        val cleanHours = listOf(1, 2, 3, 6, 12)
        val best = cleanHours.minByOrNull { abs(it - hours) } ?: 1
        return best * 60 * 60_000L
    }

    val days = this / (24 * 60 * 60_000)

    if (days < 7) {
        val cleanDays = listOf(1, 2, 4)  // 일 단위
        val best = cleanDays.minByOrNull { abs(it - days) } ?: 1
        return best * 24 * 60 * 60_000L
    }

    val weeks = days / 7

    if (weeks < 4) {
        val cleanWeeks = listOf(1, 2, 3)  // 주 단위
        val best = cleanWeeks.minByOrNull { abs(it - weeks) } ?: 1
        return best * 7 * 24 * 60 * 60_000L
    }

    val months = days / 30

    val cleanMonths = listOf(1, 3, 6, 12)
    val best = cleanMonths.minByOrNull { abs(it - months) } ?: 1
    return best * 30 * 24 * 60 * 60_000L
}