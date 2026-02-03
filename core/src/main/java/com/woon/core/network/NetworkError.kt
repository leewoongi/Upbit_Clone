package com.woon.core.network

/**
 * 공통 네트워크 에러 모델
 *
 * 모든 도메인(Candle, Market, Ticker, Trade, Event)에서 발생하는
 * 네트워크 관련 에러를 통합 표현한다.
 *
 * @property type 에러 유형
 * @property message 사용자에게 표시할 메시지
 * @property cause 원인 예외
 * @property isRetryable 자동 재시도 가능 여부
 * @property retryAfterMs 재시도 대기 시간 (Rate Limit 시)
 */
data class NetworkError(
    val type: Type,
    val message: String,
    val cause: Throwable? = null,
    val isRetryable: Boolean = true,
    val retryAfterMs: Long? = null
) {
    /**
     * 네트워크 에러 유형
     */
    enum class Type {
        /** 연결 타임아웃 (서버 응답 없음) */
        TIMEOUT,

        /** 네트워크 I/O 에러 (연결 끊김, 불안정) */
        IO,

        /** DNS 조회 실패 */
        DNS,

        /** SSL/TLS 핸드셰이크 실패 */
        SSL,

        /** Rate Limit 초과 (429) */
        RATE_LIMIT,

        /** 서버 에러 (5xx) */
        SERVER,

        /** 클라이언트 에러 (4xx, Rate Limit 제외) */
        CLIENT,

        /** 데이터 파싱 에러 */
        PARSE,

        /** WebSocket 연결 에러 */
        WEBSOCKET,

        /** 알 수 없는 에러 */
        UNKNOWN
    }

    /**
     * 사용자 친화적 에러 메시지
     */
    val userMessage: String
        get() = when (type) {
            Type.TIMEOUT -> "서버 응답이 지연되고 있습니다. 잠시 후 다시 시도해주세요."
            Type.IO -> "네트워크 연결이 불안정합니다. 연결 상태를 확인해주세요."
            Type.DNS -> "서버를 찾을 수 없습니다. 인터넷 연결을 확인해주세요."
            Type.SSL -> "보안 연결에 실패했습니다. 네트워크 설정을 확인해주세요."
            Type.RATE_LIMIT -> "요청이 너무 많습니다. 잠시 후 다시 시도해주세요."
            Type.SERVER -> "서버에 일시적인 문제가 발생했습니다."
            Type.CLIENT -> "요청을 처리할 수 없습니다."
            Type.PARSE -> "데이터 처리 중 오류가 발생했습니다."
            Type.WEBSOCKET -> "실시간 연결이 끊어졌습니다."
            Type.UNKNOWN -> "알 수 없는 오류가 발생했습니다."
        }

    /**
     * 재시도 버튼 텍스트
     */
    val retryButtonText: String
        get() = when (type) {
            Type.RATE_LIMIT -> "잠시 후 재시도"
            Type.SERVER -> "다시 시도"
            else -> "재시도"
        }

    companion object {
        fun timeout(cause: Throwable? = null) = NetworkError(
            type = Type.TIMEOUT,
            message = "Request timed out",
            cause = cause,
            isRetryable = true
        )

        fun io(cause: Throwable? = null) = NetworkError(
            type = Type.IO,
            message = "Network I/O error",
            cause = cause,
            isRetryable = true
        )

        fun dns(cause: Throwable? = null) = NetworkError(
            type = Type.DNS,
            message = "DNS lookup failed",
            cause = cause,
            isRetryable = true
        )

        fun ssl(cause: Throwable? = null) = NetworkError(
            type = Type.SSL,
            message = "SSL handshake failed",
            cause = cause,
            isRetryable = false  // SSL 에러는 자동 재시도 불가
        )

        fun rateLimit(retryAfterMs: Long? = null, cause: Throwable? = null) = NetworkError(
            type = Type.RATE_LIMIT,
            message = "Rate limit exceeded",
            cause = cause,
            isRetryable = true,
            retryAfterMs = retryAfterMs ?: DEFAULT_RATE_LIMIT_DELAY_MS
        )

        fun server(code: Int, cause: Throwable? = null) = NetworkError(
            type = Type.SERVER,
            message = "Server error: $code",
            cause = cause,
            isRetryable = true
        )

        fun client(code: Int, cause: Throwable? = null) = NetworkError(
            type = Type.CLIENT,
            message = "Client error: $code",
            cause = cause,
            isRetryable = false
        )

        fun parse(cause: Throwable? = null) = NetworkError(
            type = Type.PARSE,
            message = "Parse error",
            cause = cause,
            isRetryable = false
        )

        fun websocket(cause: Throwable? = null) = NetworkError(
            type = Type.WEBSOCKET,
            message = "WebSocket error",
            cause = cause,
            isRetryable = true
        )

        fun unknown(cause: Throwable? = null) = NetworkError(
            type = Type.UNKNOWN,
            message = cause?.message ?: "Unknown error",
            cause = cause,
            isRetryable = false
        )

        private const val DEFAULT_RATE_LIMIT_DELAY_MS = 60_000L
    }
}
