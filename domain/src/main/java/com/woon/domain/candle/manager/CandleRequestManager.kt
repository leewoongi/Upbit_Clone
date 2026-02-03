package com.woon.domain.candle.manager

import com.woon.domain.candle.entity.constant.CandleType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Candle API 요청 관리자
 *
 * Candle 연속 호출(초/분/일/주/월) 시 동시 요청을 제한한다.
 *
 * 기능:
 * - 이전 요청 자동 취소 (같은 마켓/타입)
 * - Debounce: 연속 호출 시 마지막 요청만 실행
 * - Throttle: 최소 요청 간격 보장
 * - 동시 요청 제한
 */
@Singleton
class CandleRequestManager @Inject constructor() {

    private val mutex = Mutex()
    private val activeJobs = mutableMapOf<RequestKey, Job>()
    private val lastRequestTimes = mutableMapOf<RequestKey, Long>()
    private val pendingDebounce = mutableMapOf<RequestKey, Job>()

    /**
     * 요청 키 (마켓코드 + 캔들타입)
     */
    data class RequestKey(
        val marketCode: String,
        val candleType: CandleType
    )

    /**
     * 새 요청 등록 (이전 요청 자동 취소)
     *
     * @param key 요청 키
     * @param job 새 Job
     */
    suspend fun registerRequest(key: RequestKey, job: Job) {
        mutex.withLock {
            // 이전 요청 취소
            activeJobs[key]?.cancel()
            activeJobs[key] = job
            lastRequestTimes[key] = System.currentTimeMillis()
        }

        // Job 완료 시 자동 제거
        job.invokeOnCompletion {
            activeJobs.remove(key)
        }
    }

    /**
     * 기존 요청 취소
     */
    suspend fun cancelRequest(key: RequestKey) {
        mutex.withLock {
            activeJobs[key]?.cancel()
            activeJobs.remove(key)
            pendingDebounce[key]?.cancel()
            pendingDebounce.remove(key)
        }
    }

    /**
     * 특정 마켓의 모든 요청 취소
     */
    suspend fun cancelAllForMarket(marketCode: String) {
        mutex.withLock {
            val keysToRemove = activeJobs.keys.filter { it.marketCode == marketCode }
            keysToRemove.forEach { key ->
                activeJobs[key]?.cancel()
                activeJobs.remove(key)
            }

            val debounceToRemove = pendingDebounce.keys.filter { it.marketCode == marketCode }
            debounceToRemove.forEach { key ->
                pendingDebounce[key]?.cancel()
                pendingDebounce.remove(key)
            }
        }
    }

    /**
     * 모든 요청 취소
     */
    suspend fun cancelAll() {
        mutex.withLock {
            activeJobs.values.forEach { it.cancel() }
            activeJobs.clear()
            pendingDebounce.values.forEach { it.cancel() }
            pendingDebounce.clear()
        }
    }

    /**
     * Throttle 체크
     *
     * 마지막 요청으로부터 지정 시간이 지났는지 확인
     *
     * @param key 요청 키
     * @param intervalMs 최소 요청 간격 (기본 300ms)
     * @return true면 요청 허용
     */
    suspend fun checkThrottle(key: RequestKey, intervalMs: Long = DEFAULT_THROTTLE_MS): Boolean {
        return mutex.withLock {
            val lastTime = lastRequestTimes[key] ?: 0L
            val now = System.currentTimeMillis()
            now - lastTime >= intervalMs
        }
    }

    /**
     * Debounce 적용 요청
     *
     * 연속 호출 시 마지막 요청만 실행
     *
     * @param key 요청 키
     * @param delayMs 대기 시간 (기본 500ms)
     * @param scope CoroutineScope
     * @param block 실행할 블록
     */
    suspend fun <T> debounce(
        key: RequestKey,
        delayMs: Long = DEFAULT_DEBOUNCE_MS,
        scope: kotlinx.coroutines.CoroutineScope,
        block: suspend () -> T
    ): Job {
        // 이전 대기 중인 요청 취소
        mutex.withLock {
            pendingDebounce[key]?.cancel()
        }

        val job = scope.launch {
            delay(delayMs)

            // 대기 완료 후 실행
            mutex.withLock {
                pendingDebounce.remove(key)
            }

            block()
        }

        mutex.withLock {
            pendingDebounce[key] = job
        }

        return job
    }

    /**
     * 현재 활성 요청 수
     */
    val activeRequestCount: Int
        get() = activeJobs.count { it.value.isActive }

    /**
     * 특정 요청이 진행 중인지 확인
     */
    fun isRequestActive(key: RequestKey): Boolean {
        return activeJobs[key]?.isActive == true
    }

    companion object {
        const val DEFAULT_THROTTLE_MS = 300L    // 300ms throttle
        const val DEFAULT_DEBOUNCE_MS = 500L    // 500ms debounce
        const val MAX_CONCURRENT_REQUESTS = 3   // 최대 동시 요청
    }
}

/**
 * 확장 함수: RequestKey 생성
 */
fun candleRequestKey(marketCode: String, candleType: CandleType) =
    CandleRequestManager.RequestKey(marketCode, candleType)
