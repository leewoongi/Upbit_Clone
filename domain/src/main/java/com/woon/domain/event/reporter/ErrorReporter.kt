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
     * Critical 에러인 경우에만 서버로 전송합니다.
     *
     * @param throwable 발생한 에러
     * @param screen 에러가 발생한 화면 이름
     */
    fun report(throwable: Throwable, screen: String) {
        if (!isCriticalError(throwable)) return

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
     * Critical 에러 여부를 판단합니다.
     * - CancellationException: 코루틴 취소는 정상 동작
     * - 도메인 Exception: 예상된 에러 (네트워크, 서버 에러 등)
     * - 나머지: Critical 에러
     */
    fun isCriticalError(throwable: Throwable): Boolean {
        // 코루틴 취소는 정상 동작
        if (throwable is CancellationException) return false

        // 도메인 예외는 예상된 에러이므로 critical이 아님
        if (isDomainException(throwable)) return false

        // 시스템/런타임 에러는 critical
        return when (throwable) {
            is NullPointerException,
            is IllegalStateException,
            is IndexOutOfBoundsException,
            is ConcurrentModificationException,
            is ClassCastException,
            is SecurityException,
            is IllegalArgumentException,
            is NoSuchElementException,
            is UnsupportedOperationException,
            is OutOfMemoryError,
            is StackOverflowError -> true
            else -> {
                // SQLite 에러 체크 (리플렉션 없이)
                throwable.javaClass.name.contains("SQLite")
            }
        }
    }

    /**
     * 도메인 예외인지 확인합니다.
     */
    private fun isDomainException(throwable: Throwable): Boolean {
        return throwable is CandleException ||
                throwable is MarketException ||
                throwable is TickerException ||
                throwable is TradeException
    }

    /**
     * 에러 타입을 반환합니다.
     */
    private fun getErrorType(throwable: Throwable): String {
        return when (throwable) {
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
