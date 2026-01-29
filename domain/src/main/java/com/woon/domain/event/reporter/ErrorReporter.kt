package com.woon.domain.event.reporter

import com.woon.domain.breadcrumb.model.Breadcrumb
import com.woon.domain.breadcrumb.recorder.BreadcrumbRecorder
import com.woon.domain.candle.exception.CandleException
import com.woon.domain.event.entity.ErrorEvent
import com.woon.domain.event.provider.AppInfoProvider
import com.woon.domain.event.provider.DeviceInfoProvider
import com.woon.domain.event.usecase.SendErrorEventUseCase
import com.woon.domain.market.exception.MarketException
import com.woon.domain.session.SessionIdProvider
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
    private val sendErrorEventUseCase: SendErrorEventUseCase,
    private val sessionIdProvider: SessionIdProvider,
    private val breadcrumbRecorder: BreadcrumbRecorder,
    private val appInfoProvider: AppInfoProvider,
    private val deviceInfoProvider: DeviceInfoProvider
) {
    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, _ -> }
    )

    // 이전 세션 스냅샷 (첫 에러에만 사용)
    private var previousSessionBreadcrumbs: List<Breadcrumb>? = null

    init {
        previousSessionBreadcrumbs = breadcrumbRecorder.consumePreviousSnapshot()
    }

    /**
     * 에러 리포트 (기존 호환)
     */
    fun report(throwable: Throwable, screen: String) {
        report(throwable, screen, feature = "", flow = "")
    }

    /**
     * 에러 리포트 (feature/flow 포함)
     */
    fun report(
        throwable: Throwable,
        screen: String,
        feature: String,
        flow: String = ""
    ) {
        val threadName = Thread.currentThread().name
        val isMainThread = threadName == "main" || threadName.startsWith("main")

        println("[ErrorReporter] report() called: ${throwable.javaClass.simpleName} on $screen (feature=$feature)")

        if (!shouldReport(throwable)) {
            println("[ErrorReporter] report() skipped: CancellationException")
            return
        }

        val currentBreadcrumbs = breadcrumbRecorder.getRecent(30)
        println("[ErrorReporter] Breadcrumbs count: ${currentBreadcrumbs.size}")

        // 이전 세션 breadcrumb이 있으면 앞에 붙임 (첫 에러에만)
        val allBreadcrumbs = if (previousSessionBreadcrumbs != null) {
            val combined = previousSessionBreadcrumbs!! + currentBreadcrumbs
            previousSessionBreadcrumbs = null  // 1회만 사용
            combined.takeLast(30)
        } else {
            currentBreadcrumbs
        }

        val errorType = getErrorType(throwable)
        val message = throwable.message ?: throwable.javaClass.simpleName
        val stack = getStackTraceString(throwable)

        val event = ErrorEvent(
            type = errorType,
            message = message,
            stack = stack,
            screen = screen,
            sessionId = sessionIdProvider.get(),
            breadcrumbs = allBreadcrumbs,
            appVersion = appInfoProvider.appVersion,
            buildType = appInfoProvider.buildType,
            deviceModel = deviceInfoProvider.deviceModel,
            osSdkInt = deviceInfoProvider.osSdkInt,
            thread = if (isMainThread) "main" else threadName,
            feature = feature,
            flow = flow,
            fingerprintHint = calculateFingerprintHint(errorType, stack, message)
        )

        println("[ErrorReporter] Sending error event: type=${event.type}, feature=${event.feature}, breadcrumbs=${event.breadcrumbs.size}")

        scope.launch {
            runCatching {
                sendErrorEventUseCase(event)
            }.onSuccess {
                println("[ErrorReporter] Error event sent successfully")
            }.onFailure { e ->
                println("[ErrorReporter] Failed to send error event: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun shouldReport(throwable: Throwable): Boolean {
        return throwable !is CancellationException
    }

    private fun getErrorType(throwable: Throwable): String {
        return when (throwable) {
            is CandleException -> getCandleErrorType(throwable)
            is MarketException -> getMarketErrorType(throwable)
            is TickerException -> getTickerErrorType(throwable)
            is TradeException -> getTradeErrorType(throwable)
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

    private fun getStackTraceString(throwable: Throwable): String {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        throwable.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }

    /**
     * fingerprintHint 계산
     * 규칙: "{type}|{topStackFrame}|{normalizedMessage(80)}"
     */
    private fun calculateFingerprintHint(type: String, stack: String, message: String): String {
        val topFrame = extractTopStackFrame(stack)
        val normalizedMessage = normalizeMessage(message)
        return "$type|$topFrame|$normalizedMessage"
    }

    /**
     * 스택에서 첫 번째 meaningful frame 추출
     * com.woon 패키지 프레임 우선, 없으면 첫 프레임
     */
    private fun extractTopStackFrame(stack: String): String {
        val lines = stack.lines()
        val frameLines = lines.filter { it.trimStart().startsWith("at ") }

        // com.woon 패키지의 첫 프레임 찾기
        val appFrame = frameLines.firstOrNull { it.contains(APP_PACKAGE_PREFIX) }
        if (appFrame != null) {
            return parseFrameLine(appFrame)
        }

        // 없으면 첫 번째 프레임
        return frameLines.firstOrNull()?.let { parseFrameLine(it) } ?: "unknown"
    }

    /**
     * "at com.woon.MyClass.method(MyClass.kt:42)" -> "com.woon.MyClass.method"
     */
    private fun parseFrameLine(line: String): String {
        val trimmed = line.trim().removePrefix("at ")
        val parenIndex = trimmed.indexOf('(')
        return if (parenIndex > 0) trimmed.substring(0, parenIndex) else trimmed
    }

    /**
     * 메시지 정규화: 80자로 자르고 숫자 연속을 #으로 치환
     */
    private fun normalizeMessage(message: String): String {
        return message
            .take(MAX_MESSAGE_LENGTH)
            .replace(Regex("\\d+"), "#")
    }

    companion object {
        private const val APP_PACKAGE_PREFIX = "com.woon"
        private const val MAX_MESSAGE_LENGTH = 80
    }
}
