package com.woon.core.ext

fun String.reverseParts(
    delimiter: String = "-"
): String {
    val parts = split(delimiter)
    return "${parts[1]}/${parts[0]}"
}