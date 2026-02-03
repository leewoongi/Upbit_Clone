package com.woon.domain.event.reporter

import com.woon.domain.breadcrumb.model.Breadcrumb
import com.woon.domain.breadcrumb.model.BreadcrumbType
import com.woon.domain.breadcrumb.recorder.BreadcrumbRecorder
import com.woon.domain.candle.exception.CandleException
import com.woon.domain.event.entity.ErrorEvent
import com.woon.domain.event.provider.AppInfoProvider
import com.woon.domain.event.provider.DeviceInfoProvider
import com.woon.domain.event.provider.InstallIdProvider
import com.woon.domain.event.provider.NetworkStateProvider
import com.woon.domain.event.storage.PendingErrorEventStorage
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
import kotlinx.coroutines.delay
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
    private val deviceInfoProvider: DeviceInfoProvider,
    private val pendingEventStorage: PendingErrorEventStorage,
    private val networkStateProvider: NetworkStateProvider,
    private val installIdProvider: InstallIdProvider
) {
    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, _ -> }
    )

    // 이전 세션 스냅샷 (첫 에러에만 사용)
    private var previousSessionBreadcrumbs: List<Breadcrumb>? = null

    // Fallback 이벤트 중복 방지용 (세션당 한 번만 로깅)
    private val reportedFallbacks = mutableSetOf<String>()

    init {
        previousSessionBreadcrumbs = breadcrumbRecorder.consumePreviousSnapshot()
        // 앱 시작 시 저장된 대기 이벤트 전송 시도
        scope.launch {
            delay(3000) // 앱 초기화 완료 대기
            flushPendingQueue()
        }
        startRetryLoop()
    }

    /**
     * 주기적으로 대기 큐의 이벤트 재전송 시도
     */
    private fun startRetryLoop() {
        scope.launch {
            while (true) {
                delay(RETRY_INTERVAL_MS)
                flushPendingQueue()
            }
        }
    }

    /**
     * 네트워크 복구 시 외부에서 호출하여 즉시 재전송
     */
    fun retrySavedEvents() {
        scope.launch {
            println("[ErrorReporter] retrySavedEvents() called - network restored")
            flushPendingQueue()
        }
    }

    /**
     * 대기 큐의 이벤트들을 전송 시도 (Room에서 로드)
     */
    private suspend fun flushPendingQueue() {
        val pendingEvents = pendingEventStorage.getAll()
        if (pendingEvents.isEmpty()) return

        println("[ErrorReporter] Flushing pending queue: ${pendingEvents.size} events from storage")

        val eventsToRetry = pendingEvents.take(MAX_RETRY_BATCH)

        for (event in eventsToRetry) {
            val result = sendErrorEventUseCase(event)

            result.onSuccess {
                println("[ErrorReporter] Pending event sent successfully: ${event.type}")
                // 전송 성공 시 Room에서 삭제
                pendingEventStorage.delete(event.id)
            }.onFailure { e ->
                println("[ErrorReporter] Failed to send pending event: ${e.message}")
                // 실패 시 그대로 유지 (다음 retry에서 재시도)
            }
        }
    }

    /**
     * Fallback 이벤트 로깅 (non-fatal, 세션당 한 번만 기록)
     *
     * @param errorType 에러 유형 (예: "EMPTY_CANDLES", "NULL_CAST", "NO_SUCH_ELEMENT")
     * @param feature 기능 (예: "Grid", "TimeScale", "TradingChart")
     * @param screen 화면 (예: "DetailScreen", "HomeScreen")
     * @param topFrameHint 발생 위치 힌트 (예: "Grid.drawGrid")
     * @param extra 추가 정보 (예: mapOf("dataSize" to "0", "key" to "visibleCandles"))
     */
    fun reportFallback(
        errorType: String,
        feature: String,
        screen: String,
        topFrameHint: String,
        extra: Map<String, String> = emptyMap()
    ) {
        val fallbackKey = "$errorType|$feature|$screen|$topFrameHint"

        // 세션당 동일 fallback은 한 번만 기록
        if (fallbackKey in reportedFallbacks) {
            return
        }
        reportedFallbacks.add(fallbackKey)

        println("[ErrorReporter] Fallback recorded: $fallbackKey, extra=$extra")

        breadcrumbRecorder.recordSystem(
            name = "Fallback",
            attrs = mapOf(
                "errorType" to errorType,
                "feature" to feature,
                "screen" to screen,
                "topFrameHint" to topFrameHint,
                "isFallback" to "true"
            ) + extra
        )
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
        val previousBreadcrumbs = previousSessionBreadcrumbs
        val allBreadcrumbs = if (previousBreadcrumbs != null) {
            val combined = previousBreadcrumbs + currentBreadcrumbs
            previousSessionBreadcrumbs = null  // 1회만 사용
            combined.takeLast(30)
        } else {
            currentBreadcrumbs
        }

        val errorType = getErrorType(throwable)
        val message = throwable.message ?: throwable.javaClass.simpleName
        val stack = getStackTraceString(throwable)
        val topFrame = extractTopStackFrame(stack)
        val normalizedMessage = normalizeMessage(message)

        val event = ErrorEvent(
            type = errorType,
            message = message,
            stack = stack,
            screen = screen,
            sessionId = sessionIdProvider.get(),
            breadcrumbs = allBreadcrumbs,

            // App/Device 정보
            appVersion = appInfoProvider.appVersion,
            buildType = appInfoProvider.buildType,
            deviceModel = deviceInfoProvider.deviceModel,
            osSdkInt = deviceInfoProvider.osSdkInt,
            locale = appInfoProvider.locale,
            installId = installIdProvider.installId,

            // Context 정보
            thread = threadName,
            isMainThread = isMainThread,
            feature = feature,
            flow = flow,

            // Network 정보
            networkType = networkStateProvider.networkType,
            isAirplaneMode = networkStateProvider.isAirplaneModeOn,

            // LLM Hint 필드
            exceptionClass = throwable.javaClass.name,
            topFrameHint = topFrame,
            messageNormalizedHint = normalizedMessage,
            breadcrumbsSummaryHint = generateBreadcrumbsSummary(allBreadcrumbs),
            fingerprintHint = "$errorType|$topFrame|$normalizedMessage"
        )

        println("[ErrorReporter] Sending error event: type=${event.type}, feature=${event.feature}, breadcrumbs=${event.breadcrumbs.size}")

        scope.launch {
            sendWithRetry(event)
        }
    }

    /**
     * 이벤트 전송, 실패 시 Room에 저장
     */
    private suspend fun sendWithRetry(event: ErrorEvent) {
        val result = sendErrorEventUseCase(event)

        result.onSuccess {
            println("[ErrorReporter] Error event sent successfully")
        }.onFailure { e ->
            val networkState = networkStateProvider.getNetworkStateDescription()
            println("[ErrorReporter] Failed to send error event: ${e.message}")
            println("[ErrorReporter] Network state: $networkState")
            // Room에 저장 (PendingErrorEventStorageImpl에서 최대 10개 제한 관리)
            runCatching {
                pendingEventStorage.save(event)
                val count = pendingEventStorage.count()
                println("[ErrorReporter] Event saved to storage for retry. Storage count: $count")
            }.onFailure { saveError ->
                println("[ErrorReporter] Failed to save event to storage: ${saveError.message}")
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
     * 메시지 정규화: 80자로 자르고 숫자/UUID를 #으로 치환
     */
    private fun normalizeMessage(message: String): String {
        return message
            .take(MAX_MESSAGE_LENGTH)
            .replace(Regex("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}", RegexOption.IGNORE_CASE), "#UUID#")
            .replace(Regex("\\d+"), "#")
    }

    /**
     * Breadcrumbs를 사람이 읽기 쉬운 한 줄 요약으로 변환
     * 예: "HomeScreen -> CoinItem(BTC) -> DetailScreen -> TimeFrameChange(1m->1M)"
     */
    private fun generateBreadcrumbsSummary(breadcrumbs: List<Breadcrumb>): String {
        if (breadcrumbs.isEmpty()) return ""

        // 최근 5개의 중요한 이벤트만 요약
        val recentSignificant = breadcrumbs
            .filter { it.type != BreadcrumbType.HTTP }  // HTTP는 제외
            .takeLast(5)

        if (recentSignificant.isEmpty()) return ""

        return recentSignificant.joinToString(" -> ") { bc ->
            when (bc.type) {
                BreadcrumbType.SCREEN -> bc.name
                BreadcrumbType.NAV -> "Nav(${bc.name.substringAfterLast("/")})"
                BreadcrumbType.CLICK -> {
                    val param = bc.attrs.values.firstOrNull()?.take(10) ?: ""
                    if (param.isNotEmpty()) "${bc.name}($param)" else bc.name
                }
                else -> bc.name
            }
        }.take(MAX_SUMMARY_LENGTH)
    }

    companion object {
        private const val APP_PACKAGE_PREFIX = "com.woon"
        private const val MAX_MESSAGE_LENGTH = 80
        private const val MAX_SUMMARY_LENGTH = 200
        private const val MAX_RETRY_BATCH = 3
        private const val RETRY_INTERVAL_MS = 30_000L  // 30초마다 재시도
    }
}
