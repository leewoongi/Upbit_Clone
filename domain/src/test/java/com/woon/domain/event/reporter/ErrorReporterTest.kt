package com.woon.domain.event.reporter

import com.woon.domain.breadcrumb.model.Breadcrumb
import com.woon.domain.breadcrumb.recorder.BreadcrumbRecorder
import com.woon.domain.event.entity.ErrorEvent
import com.woon.domain.event.provider.AppInfoProvider
import com.woon.domain.event.provider.DeviceInfoProvider
import com.woon.domain.event.provider.InstallIdProvider
import com.woon.domain.event.provider.NetworkStateProvider
import com.woon.domain.event.storage.PendingErrorEventStorage
import com.woon.domain.event.usecase.SendErrorEventUseCase
import com.woon.domain.session.SessionIdProvider
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * ErrorReporter 안정성 테스트
 *
 * - reportFallback이 중복 로깅을 방지하는지 검증
 * - null 입력에 안전하게 처리되는지 검증
 */
class ErrorReporterTest {

    private lateinit var errorReporter: ErrorReporter
    private lateinit var fakeBreadcrumbRecorder: FakeBreadcrumbRecorder
    private lateinit var fakeSendErrorEventUseCase: FakeSendErrorEventUseCase
    private lateinit var fakePendingStorage: FakePendingErrorEventStorage

    @Before
    fun setup() {
        fakeBreadcrumbRecorder = FakeBreadcrumbRecorder()
        fakeSendErrorEventUseCase = FakeSendErrorEventUseCase()
        fakePendingStorage = FakePendingErrorEventStorage()

        errorReporter = ErrorReporter(
            sendErrorEventUseCase = fakeSendErrorEventUseCase,
            sessionIdProvider = FakeSessionIdProvider(),
            breadcrumbRecorder = fakeBreadcrumbRecorder,
            appInfoProvider = FakeAppInfoProvider(),
            deviceInfoProvider = FakeDeviceInfoProvider(),
            pendingEventStorage = fakePendingStorage,
            networkStateProvider = FakeNetworkStateProvider(),
            installIdProvider = FakeInstallIdProvider()
        )
    }

    // ===================================
    // reportFallback 테스트
    // ===================================

    @Test
    fun `reportFallback should record system breadcrumb`() {
        // When
        errorReporter.reportFallback(
            errorType = "EMPTY_CANDLES",
            feature = "Grid",
            screen = "DetailScreen",
            topFrameHint = "Grid.drawGrid",
            extra = mapOf("dataSize" to "0")
        )

        // Then
        assertEquals(1, fakeBreadcrumbRecorder.systemRecords.size)
        val record = fakeBreadcrumbRecorder.systemRecords.first()
        assertEquals("Fallback", record.first)
        assertTrue(record.second.containsKey("errorType"))
        assertEquals("EMPTY_CANDLES", record.second["errorType"])
        assertEquals("true", record.second["isFallback"])
    }

    @Test
    fun `reportFallback should deduplicate same fallback`() {
        // When - same fallback reported twice
        errorReporter.reportFallback(
            errorType = "EMPTY_CANDLES",
            feature = "Grid",
            screen = "DetailScreen",
            topFrameHint = "Grid.drawGrid"
        )
        errorReporter.reportFallback(
            errorType = "EMPTY_CANDLES",
            feature = "Grid",
            screen = "DetailScreen",
            topFrameHint = "Grid.drawGrid"
        )

        // Then - only recorded once
        assertEquals(1, fakeBreadcrumbRecorder.systemRecords.size)
    }

    @Test
    fun `reportFallback should allow different fallbacks`() {
        // When - different fallbacks
        errorReporter.reportFallback(
            errorType = "EMPTY_CANDLES",
            feature = "Grid",
            screen = "DetailScreen",
            topFrameHint = "Grid.drawGrid"
        )
        errorReporter.reportFallback(
            errorType = "NULL_CAST",
            feature = "TimeScale",
            screen = "DetailScreen",
            topFrameHint = "TimeScale.draw"
        )

        // Then - both recorded
        assertEquals(2, fakeBreadcrumbRecorder.systemRecords.size)
    }

    @Test
    fun `reportFallback should include extra data`() {
        // When
        errorReporter.reportFallback(
            errorType = "NO_SUCH_ELEMENT",
            feature = "TradingChartState",
            screen = "DetailScreen",
            topFrameHint = "TradingChartState.visibleTimeRangeMs",
            extra = mapOf("dataSize" to "0", "key" to "visibleCandles")
        )

        // Then
        val record = fakeBreadcrumbRecorder.systemRecords.first()
        assertEquals("0", record.second["dataSize"])
        assertEquals("visibleCandles", record.second["key"])
    }

    // ===================================
    // report 안전성 테스트
    // ===================================

    @Test
    fun `report should handle null message gracefully`() {
        // Given - exception with null message
        val exception = RuntimeException(null as String?)

        // When & Then - no crash
        errorReporter.report(exception, "TestScreen")
    }

    @Test
    fun `report should handle empty stack trace`() {
        // Given - exception with minimal info
        val exception = RuntimeException("test")

        // When & Then - no crash
        errorReporter.report(exception, "TestScreen", feature = "TestFeature")
    }

    // ===================================
    // Fake Implementations
    // ===================================

    private class FakeBreadcrumbRecorder : BreadcrumbRecorder {
        val systemRecords = mutableListOf<Pair<String, Map<String, String>>>()

        override fun recordScreen(name: String) {}
        override fun recordNav(route: String) {}
        override fun recordClick(name: String, attrs: Map<String, String>) {}
        override fun recordHttp(method: String, url: String, statusCode: Int?, durationMs: Long?, extra: Map<String, String>) {}
        override fun recordSystem(name: String, attrs: Map<String, String>) {
            systemRecords.add(name to attrs)
        }
        override fun getRecent(limit: Int): List<Breadcrumb> = emptyList()
        override fun consumePreviousSnapshot(): List<Breadcrumb>? = null
        override fun saveSnapshot() {}
    }

    private class FakeSendErrorEventUseCase : SendErrorEventUseCase {
        override suspend fun invoke(event: ErrorEvent): Result<Unit> = Result.success(Unit)
    }

    private class FakeSessionIdProvider : SessionIdProvider {
        override fun get(): String = "test-session"
    }

    private class FakeAppInfoProvider : AppInfoProvider {
        override val appVersion: String = "1.0.0"
        override val buildType: String = "debug"
        override val locale: String = "ko-KR"
    }

    private class FakeDeviceInfoProvider : DeviceInfoProvider {
        override val deviceModel: String = "test-device"
        override val osSdkInt: Int = 30
    }

    private class FakePendingErrorEventStorage : PendingErrorEventStorage {
        override suspend fun save(event: ErrorEvent) {}
        override suspend fun getAll(): List<ErrorEvent> = emptyList()
        override suspend fun delete(id: String) {}
        override suspend fun count(): Int = 0
    }

    private class FakeNetworkStateProvider : NetworkStateProvider {
        override val isAirplaneModeOn: Boolean = false
        override val isWifiConnected: Boolean = true
        override val isMobileDataConnected: Boolean = false
        override val isNetworkAvailable: Boolean = true
        override val networkType: String = "wifi"
        override fun getNetworkStateDescription(): String = "wifi=ON"
    }

    private class FakeInstallIdProvider : InstallIdProvider {
        override val installId: String = "test-install-id"
    }
}
