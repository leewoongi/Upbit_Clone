package com.woon.domain.market.exception

sealed class MarketException(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message, cause) {

    class NetworkException(
        message: String = "네트워크 연결에 실패했습니다",
        cause: Throwable? = null
    ) : MarketException(message, cause)

    class TimeoutException(
        message: String = "요청 시간이 초과되었습니다",
        cause: Throwable? = null
    ) : MarketException(message, cause)

    class ParseException(
        message: String = "데이터 파싱에 실패했습니다",
        cause: Throwable? = null
    ) : MarketException(message, cause)

    class RateLimitExceededException(
        message: String = "요청 횟수가 초과되었습니다. 잠시 후 다시 시도해주세요",
        cause: Throwable? = null
    ) : MarketException(message, cause)

    class ServerException(
        val code: Int,
        message: String = "서버 에러가 발생했습니다 (code: $code)",
        cause: Throwable? = null
    ) : MarketException(message, cause)

    class ClientException(
        val code: Int,
        message: String = "요청 에러가 발생했습니다 (code: $code)",
        cause: Throwable? = null
    ) : MarketException(message, cause)

    class EmptyDataException(
        message: String = "마켓 데이터가 없습니다",
        cause: Throwable? = null
    ) : MarketException(message, cause)

    class SSLException(
        message: String = "보안 연결에 실패했습니다",
        cause: Throwable? = null
    ) : MarketException(message, cause)

    class UnknownException(
        message: String = "알 수 없는 에러가 발생했습니다",
        cause: Throwable? = null
    ) : MarketException(message, cause)
}
