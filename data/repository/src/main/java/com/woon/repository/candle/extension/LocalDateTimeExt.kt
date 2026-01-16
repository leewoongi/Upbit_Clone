package com.woon.repository.candle.extension

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal fun String.toDate(): Date {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    return format.parse(this) ?: Date()
}

internal fun Long.toDate(): Date {
    return Date(this)
}

