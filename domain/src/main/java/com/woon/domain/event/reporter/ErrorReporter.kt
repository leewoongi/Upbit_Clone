package com.woon.domain.event.reporter

import com.woon.domain.candle.exception.CandleException
import com.woon.domain.event.entity.ErrorEvent
import com.woon.domain.event.usecase.SendErrorEventUseCase
import com.woon.domain.market.exception.MarketException
import com.woon.domain.ticker.exception.TickerException
import com.woon.domain.trade.exception.TradeException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.PrintWriter
import java.io.StringWriter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorReporter @Inject constructor(
    private val sendErrorEventUseCase: SendErrorEventUseCase
) {
    /**
     * 자체 CoroutineScope
     * - 전역 크래시 핸들러에서도 사용 가능
     * - SupervisorJob: 하나의 에러 전송 실패가 다른 전송에 영향 X
     * - CoroutineExceptionHandler: 에러 전송 중 예외 발생 시 무시
     */
    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, _ -> }
    )

    /**
     * 에러를 리포트합니다.
     * CancellationException을 제외한 모든 에러를 서버로 전송합니다.
     *
     * @param throwable 발생한 에러
     * @param screen 에러가 발생한 화면 이름
     */
    fun report(throwable: Throwable, screen: String) {
        if (!shouldReport(throwable)) return

        val event = ErrorEvent(
            type = getErrorType(throwable),
            message = throwable.message ?: throwable.javaClass.simpleName,
            stack = getStackTraceString(throwable),
            screen = screen
        )

        scope.launch {
            runCatching {
                sendErrorEventUseCase(event)
            }
        }
    }

    /**
     * 리포트 여부를 판단합니다.
     * - CancellationException: 코루틴 취소는 정상 동작이므로 제외
     * - 나머지 모든 에러: 서버로 전송
     */
    private fun shouldReport(throwable: Throwable): Boolean {
        // 코루틴 취소는 정상 동작이므로 리포트하지 않음
        return throwable !is CancellationException
    }

    /**
     * 에러 타입을 반환합니다.
     */
    private fun getErrorType(throwable: Throwable): String {
        return when (throwable) {
            // 도메인 예외
            is CandleException -> getCandleErrorType(throwable)
            is MarketException -> getMarketErrorType(throwable)
            is TickerException -> getTickerErrorType(throwable)
            is TradeException -> getTradeErrorType(throwable)
            // 시스템/런타임 에러
            is NullPointerException -> "NULL_POINTER"
            is IllegalStateException -> "ILLEGAL_STATE"
            is IndexOutOfBoundsException -> "INDEX_OUT_OF_BOUNDS"
            is ConcurrentModificationException -> "CONCURRENT_MODIFICATION"
            is ClassCastException -> "CLASS_CAST"
            is SecurityException -> "SECURITY"
            is IllegalArgumentException -> "ILLEGAL_ARGUMENT"
            is NoSuchElementException -> "NO_SUCH_ELEMENT"
            is UnsupportedOperationException -> "UNSUPPORTED_OPERATION"
            is OutOfMemoryError -> "OUT_OF_MEMORY"
            is StackOverflowError -> "STACK_OVERFLOW"
            else -> "UNKNOWN"
        }
    }

    private fun getCandleErrorType(e: CandleException): String {
        return when (e) {
            is CandleException.NetworkException -> "CANDLE_NETWORK"
            is CandleException.TimeoutException -> "CANDLE_TIMEOUT"
            is CandleException.RateLimitExceededException -> "CANDLE_RATE_LIMIT"
            is CandleException.ServerException -> "CANDLE_SERVER"
            is CandleException.ClientException -> "CANDLE_CLIENT"
            is CandleException.ParseException -> "CANDLE_PARSE"
            is CandleException.InvalidMarketCodeException -> "CANDLE_INVALID_MARKET_CODE"
            is CandleException.InvalidParameterException -> "CANDLE_INVALID_PARAMETER"
            is CandleException.WebSocketConnectionException -> "CANDLE_WEBSOCKET_CONNECTION"
            is CandleException.WebSocketDisconnectedException -> "CANDLE_WEBSOCKET_DISCONNECTED"
            is CandleException.EmptyDataException -> "CANDLE_EMPTY_DATA"
            is CandleException.SSLException -> "CANDLE_SSL"
            is CandleException.UnknownException -> "CANDLE_UNKNOWN"
        }
    }

    private fun getMarketErrorType(e: MarketException): String {
        return when (e) {
            is MarketException.NetworkException -> "MARKET_NETWORK"
            is MarketException.TimeoutException -> "MARKET_TIMEOUT"
            is MarketException.RateLimitExceededException -> "MARKET_RATE_LIMIT"
            is MarketException.ServerException -> "MARKET_SERVER"
            is MarketException.ClientException -> "MARKET_CLIENT"
            is MarketException.ParseException -> "MARKET_PARSE"
            is MarketException.EmptyDataException -> "MARKET_EMPTY_DATA"
            is MarketException.SSLException -> "MARKET_SSL"
            is MarketException.UnknownException -> "MARKET_UNKNOWN"
        }
    }

    private fun getTickerErrorType(e: TickerException): String {
        return when (e) {
            is TickerException.NetworkException -> "TICKER_NETWORK"
            is TickerException.TimeoutException -> "TICKER_TIMEOUT"
            is TickerException.ParseException -> "TICKER_PARSE"
            is TickerException.WebSocketConnectionException -> "TICKER_WEBSOCKET_CONNECTION"
            is TickerException.WebSocketDisconnectedException -> "TICKER_WEBSOCKET_DISCONNECTED"
            is TickerException.InvalidMarketCodeException -> "TICKER_INVALID_MARKET_CODE"
            is TickerException.EmptyDataException -> "TICKER_EMPTY_DATA"
            is TickerException.SSLException -> "TICKER_SSL"
            is TickerException.UnknownException -> "TICKER_UNKNOWN"
        }
    }

    private fun getTradeErrorType(e: TradeException): String {
        return when (e) {
            is TradeException.NetworkException -> "TRADE_NETWORK"
            is TradeException.TimeoutException -> "TRADE_TIMEOUT"
            is TradeException.ParseException -> "TRADE_PARSE"
            is TradeException.WebSocketConnectionException -> "TRADE_WEBSOCKET_CONNECTION"
            is TradeException.WebSocketDisconnectedException -> "TRADE_WEBSOCKET_DISCONNECTED"
            is TradeException.InvalidMarketCodeException -> "TRADE_INVALID_MARKET_CODE"
            is TradeException.EmptyDataException -> "TRADE_EMPTY_DATA"
            is TradeException.SSLException -> "TRADE_SSL"
            is TradeException.UnknownException -> "TRADE_UNKNOWN"
        }
    }

    /**
     * Stack trace를 문자열로 변환합니다.
     */
    private fun getStackTraceString(throwable: Throwable): String {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        throwable.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }
}
