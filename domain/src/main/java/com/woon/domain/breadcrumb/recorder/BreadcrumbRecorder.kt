package com.woon.domain.breadcrumb.recorder

import com.woon.domain.breadcrumb.model.Breadcrumb
import com.woon.domain.breadcrumb.model.BreadcrumbType
import com.woon.domain.breadcrumb.storage.BreadcrumbStorage
import com.woon.domain.session.SessionIdProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.ArrayDeque
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BreadcrumbRecorder @Inject constructor(
    private val sessionIdProvider: SessionIdProvider,
    private val storage: BreadcrumbStorage
) {
    private val buffer = ArrayDeque<Breadcrumb>(MAX_SIZE)
    private val lock = Any()
    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, _ -> }
    )

    init {
        startPeriodicSnapshot()
    }

    fun recordScreen(screenName: String, attrs: Map<String, String> = emptyMap()) {
        record(BreadcrumbType.SCREEN, screenName, attrs)
    }

    fun recordClick(name: String, attrs: Map<String, String> = emptyMap()) {
        record(BreadcrumbType.CLICK, name, attrs)
    }

    fun recordNav(route: String, attrs: Map<String, String> = emptyMap()) {
        record(BreadcrumbType.NAV, route, attrs)
    }

    fun recordHttp(
        method: String,
        path: String,
        statusCode: Int,
        attrs: Map<String, String> = emptyMap()
    ) {
        record(
            type = BreadcrumbType.HTTP,
            name = "$method $path",
            attrs = attrs + ("status" to statusCode.toString())
        )
    }

    fun recordSystem(name: String, attrs: Map<String, String> = emptyMap()) {
        record(BreadcrumbType.SYSTEM, name, attrs)
    }

    fun getRecent(limit: Int = DEFAULT_LIMIT): List<Breadcrumb> {
        synchronized(lock) {
            return buffer.toList().takeLast(limit)
        }
    }

    /**
     * 앱 재시작 시 이전 스냅샷 로드 후 삭제
     * 1시간 이상 지난 스냅샷은 null 반환
     */
    fun consumePreviousSnapshot(): List<Breadcrumb>? {
        val result = storage.loadSnapshot()
        storage.clearSnapshot()

        if (result == null) return null

        val (breadcrumbs, snapshotTime) = result
        val age = System.currentTimeMillis() - snapshotTime

        // 1시간 이상 지난 스냅샷은 버림
        if (age > SNAPSHOT_MAX_AGE_MS) return null

        return breadcrumbs.ifEmpty { null }
    }

    private fun record(
        type: BreadcrumbType,
        name: String,
        attrs: Map<String, String>
    ) {
        val breadcrumb = Breadcrumb(
            ts = System.currentTimeMillis(),
            type = type,
            name = name.take(MAX_NAME_LENGTH),
            attrs = sanitizeAttrs(attrs),
            sessionId = sessionIdProvider.get()
        )

        synchronized(lock) {
            if (buffer.size >= MAX_SIZE) {
                buffer.removeFirst()
            }
            buffer.addLast(breadcrumb)
        }
    }

    private fun sanitizeAttrs(attrs: Map<String, String>): Map<String, String> {
        return attrs.entries
            .take(MAX_ATTRS_COUNT)
            .associate { (k, v) ->
                k.take(MAX_ATTR_KEY_LENGTH) to v.take(MAX_ATTR_VALUE_LENGTH)
            }
    }

    private fun startPeriodicSnapshot() {
        scope.launch {
            while (true) {
                delay(SNAPSHOT_INTERVAL_MS)
                saveSnapshot()
            }
        }
    }

    private fun saveSnapshot() {
        val snapshot = synchronized(lock) { buffer.toList() }
        if (snapshot.isEmpty()) return
        storage.saveSnapshot(snapshot)
    }

    companion object {
        private const val MAX_SIZE = 50
        private const val DEFAULT_LIMIT = 30

        // Sanitize 제한
        private const val MAX_NAME_LENGTH = 100
        private const val MAX_ATTR_KEY_LENGTH = 30
        private const val MAX_ATTR_VALUE_LENGTH = 100
        private const val MAX_ATTRS_COUNT = 5

        // 스냅샷
        private const val SNAPSHOT_INTERVAL_MS = 10_000L
        private const val SNAPSHOT_MAX_AGE_MS = 3600_000L  // 1시간
    }
}
