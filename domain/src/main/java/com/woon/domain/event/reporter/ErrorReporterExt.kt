package com.woon.domain.event.reporter

/**
 * 에러 리포트 확장 함수
 *
 * 사용 예시:
 * ```
 * catch { e ->
 *     e.reportIfCritical(errorReporter, "HomeScreen")
 * }
 * ```
 */
fun Throwable.reportIfCritical(errorReporter: ErrorReporter, screen: String) {
    errorReporter.report(this, screen)
}

/**
 * runCatching과 함께 사용하는 확장 함수
 *
 * 사용 예시:
 * ```
 * runCatching {
 *     someUseCase()
 * }.onFailureReport(errorReporter, "HomeScreen")
 * ```
 */
inline fun <T> Result<T>.onFailureReport(
    errorReporter: ErrorReporter,
    screen: String,
    action: (Throwable) -> Unit = {}
): Result<T> {
    return onFailure { e ->
        errorReporter.report(e, screen)
        action(e)
    }
}
