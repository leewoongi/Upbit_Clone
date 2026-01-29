package com.woon.domain.ticker.exception

sealed class TickerException(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message, cause) {

    class NetworkException(
        message: String = "네트워크 연결에 실패했습니다",
        cause: Throwable? = null
    ) : TickerException(message, cause)

    class TimeoutException(
        message: String = "요청 시간이 초과되었습니다",
        cause: Throwable? = null
    ) : TickerException(message, cause)

    class ParseException(
        message: String = "데이터 파싱에 실패했습니다",
        cause: Throwable? = null
    ) : TickerException(message, cause)

    class WebSocketConnectionException(
        message: String = "WebSocket 연결에 실패했습니다",
        cause: Throwable? = null
    ) : TickerException(message, cause)

    class WebSocketDisconnectedException(
        message: String = "WebSocket 연결이 끊어졌습니다",
        cause: Throwable? = null
    ) : TickerException(message, cause)

    class InvalidMarketCodeException(
        marketCode: String,
        cause: Throwable? = null
    ) : TickerException("잘못된 마켓 코드입니다: $marketCode", cause)

    class EmptyDataException(
        message: String = "티커 데이터가 없습니다",
        cause: Throwable? = null
    ) : TickerException(message, cause)

    class SSLException(
        message: String = "보안 연결에 실패했습니다",
        cause: Throwable? = null
    ) : TickerException(message, cause)

    class UnknownException(
        message: String = "알 수 없는 에러가 발생했습니다",
        cause: Throwable? = null
    ) : TickerException(message, cause)
}
