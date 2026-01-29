package com.woon.domain.candle.exception

sealed class CandleException(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message, cause) {

    /**
     * 네트워크 연결 실패 (인터넷 끊김, DNS 실패 등)
     */
    class NetworkException(
        message: String = "네트워크 연결에 실패했습니다",
        cause: Throwable? = null
    ) : CandleException(message, cause)

    /**
     * 요청 타임아웃
     */
    class TimeoutException(
        message: String = "요청 시간이 초과되었습니다",
        cause: Throwable? = null
    ) : CandleException(message, cause)

    /**
     * 잘못된 마켓 코드
     */
    class InvalidMarketCodeException(
        marketCode: String,
        cause: Throwable? = null
    ) : CandleException("잘못된 마켓 코드입니다: $marketCode", cause)

    /**
     * 잘못된 파라미터 (count, to 등)
     */
    class InvalidParameterException(
        message: String = "잘못된 파라미터입니다",
        cause: Throwable? = null
    ) : CandleException(message, cause)

    /**
     * 데이터 파싱 에러 (JSON 파싱 실패)
     */
    class ParseException(
        message: String = "데이터 파싱에 실패했습니다",
        cause: Throwable? = null
    ) : CandleException(message, cause)

    /**
     * WebSocket 연결 에러
     */
    class WebSocketConnectionException(
        message: String = "WebSocket 연결에 실패했습니다",
        cause: Throwable? = null
    ) : CandleException(message, cause)

    /**
     * WebSocket 연결 끊김
     */
    class WebSocketDisconnectedException(
        message: String = "WebSocket 연결이 끊어졌습니다",
        cause: Throwable? = null
    ) : CandleException(message, cause)

    /**
     * Rate Limit 초과 (429)
     */
    class RateLimitExceededException(
        message: String = "요청 횟수가 초과되었습니다. 잠시 후 다시 시도해주세요",
        cause: Throwable? = null
    ) : CandleException(message, cause)

    /**
     * 서버 에러 (5xx)
     */
    class ServerException(
        val code: Int,
        message: String = "서버 에러가 발생했습니다 (code: $code)",
        cause: Throwable? = null
    ) : CandleException(message, cause)

    /**
     * 클라이언트 에러 (4xx, Rate Limit 제외)
     */
    class ClientException(
        val code: Int,
        message: String = "요청 에러가 발생했습니다 (code: $code)",
        cause: Throwable? = null
    ) : CandleException(message, cause)

    /**
     * 데이터 없음
     */
    class EmptyDataException(
        message: String = "캔들 데이터가 없습니다",
        cause: Throwable? = null
    ) : CandleException(message, cause)

    /**
     * SSL/TLS 에러
     */
    class SSLException(
        message: String = "보안 연결에 실패했습니다",
        cause: Throwable? = null
    ) : CandleException(message, cause)

    /**
     * 알 수 없는 에러
     */
    class UnknownException(
        message: String = "알 수 없는 에러가 발생했습니다",
        cause: Throwable? = null
    ) : CandleException(message, cause)
}
