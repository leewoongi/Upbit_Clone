package com.woon.domain.candle.entity.constant

enum class Market(
    val code: String
) {
    KRW_BTC("KRW-BTC");

    companion object {
        fun fromCode(code: String): Market? {
            // values()는 모든 enum 상수를 배열로 반환합니다.
            // find는 조건에 맞는 첫 번째 요소를 찾고, 없으면 null을 반환합니다.
            return entries.find { it.code.equals(code, ignoreCase = true) }
        }
    }
}