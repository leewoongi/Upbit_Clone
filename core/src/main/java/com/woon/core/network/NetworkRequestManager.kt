package com.woon.core.network

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * 네트워크 요청 관리자
 *
 * 중복 요청 방지, 이전 요청 취소, debounce/throttle을 제공한다.
 *
 * @param T 응답 데이터 타입
 */
class NetworkRequestManager<T> {

    private val mutex = Mutex()
    private var currentJob: Job? = null
    private var lastRequestTime = 0L

    private val _state = MutableStateFlow<NetworkUiState<T>>(NetworkUiState.Idle)
    val state: StateFlow<NetworkUiState<T>> = _state.asStateFlow()

    /**
     * 현재 진행 중인 요청 취소
     */
    fun cancelCurrentRequest() {
        currentJob?.cancel()
        currentJob = null
    }

    /**
     * 새 요청 등록 (이전 요청 자동 취소)
     */
    suspend fun setCurrentJob(job: Job) {
        mutex.withLock {
            currentJob?.cancel()
            currentJob = job
        }
    }

    /**
     * 요청 완료 처리
     */
    fun clearCurrentJob() {
        currentJob = null
    }

    /**
     * 상태 업데이트
     */
    fun updateState(newState: NetworkUiState<T>) {
        _state.value = newState
    }

    /**
     * Throttle 체크 (지정 간격 내 중복 요청 차단)
     *
     * @param intervalMs 최소 요청 간격
     * @return true면 요청 허용, false면 차단
     */
    fun checkThrottle(intervalMs: Long): Boolean {
        val now = System.currentTimeMillis()
        if (now - lastRequestTime < intervalMs) {
            return false
        }
        lastRequestTime = now
        return true
    }

    /**
     * 현재 로딩 중인지 확인
     */
    val isLoading: Boolean
        get() = _state.value is NetworkUiState.Loading

    /**
     * 현재 요청 진행 중인지 확인
     */
    val hasActiveRequest: Boolean
        get() = currentJob?.isActive == true

    companion object {
        const val DEFAULT_THROTTLE_MS = 300L
        const val DEFAULT_DEBOUNCE_MS = 500L
    }
}

/**
 * 여러 요청을 관리하는 매니저
 *
 * 키별로 요청을 구분하여 관리한다.
 */
class MultiRequestManager {

    private val jobs = mutableMapOf<String, Job>()
    private val mutex = Mutex()

    /**
     * 특정 키의 요청 취소
     */
    suspend fun cancel(key: String) {
        mutex.withLock {
            jobs[key]?.cancel()
            jobs.remove(key)
        }
    }

    /**
     * 모든 요청 취소
     */
    suspend fun cancelAll() {
        mutex.withLock {
            jobs.values.forEach { it.cancel() }
            jobs.clear()
        }
    }

    /**
     * 요청 등록 (같은 키의 이전 요청은 취소)
     */
    suspend fun register(key: String, job: Job) {
        mutex.withLock {
            jobs[key]?.cancel()
            jobs[key] = job
        }
    }

    /**
     * 요청 완료 처리
     */
    suspend fun complete(key: String) {
        mutex.withLock {
            jobs.remove(key)
        }
    }

    /**
     * 특정 키의 요청이 진행 중인지 확인
     */
    fun isActive(key: String): Boolean {
        return jobs[key]?.isActive == true
    }

    /**
     * 활성 요청 수
     */
    val activeCount: Int
        get() = jobs.count { it.value.isActive }
}
