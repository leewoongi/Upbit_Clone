package com.woon.chart.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * TradingChartState 안정성 테스트
 *
 * - empty list 입력 시 크래시 없이 안전하게 처리되는지 검증
 * - 잘못된 데이터 입력 시 기본값으로 fallback 되는지 검증
 */
class TradingChartStateTest {

    private lateinit var state: TradingChartState

    @Before
    fun setup() {
        state = TradingChartState(
            visibleCount = 100,
            candleSpacing = 0.2f,
            minVisibleCount = 24,
            maxVisibleCount = 120,
            startPadding = 0.1f
        )
        state.screenWidth = 1000f
        state.screenHeight = 500f
    }

    // ===================================
    // Empty List 안전성 테스트
    // ===================================

    @Test
    fun `empty candles should not crash and return safe defaults`() {
        // Given
        state.candles = emptyList()

        // When & Then - no crash
        assertTrue(state.visibleCandles.isEmpty())
        assertEquals(0, state.visibleStartIndex)
        assertEquals(0, state.visibleEndIndex)
    }

    @Test
    fun `empty candles should return safe price range`() {
        // Given
        state.candles = emptyList()

        // When & Then
        assertEquals(0.0, state.minPrice, 0.001)
        assertEquals(0.0, state.maxPrice, 0.001)
        assertEquals(0.0, state.priceRange, 0.001)
    }

    @Test
    fun `empty candles should return safe time range`() {
        // Given
        state.candles = emptyList()

        // When & Then
        assertEquals(0L, state.visibleTimeRangeMs)
        assertEquals(60_000L, state.candleIntervalMs) // default 1분
    }

    @Test
    fun `priceToY should return center when priceRange is zero`() {
        // Given
        state.candles = emptyList()

        // When
        val y = state.priceToY(100.0)

        // Then - should return center (screenHeight / 2)
        assertEquals(250f, y, 0.001f)
    }

    @Test
    fun `yToPrice should return zero when screenHeight is zero`() {
        // Given
        state.screenHeight = 0f

        // When
        val price = state.yToPrice(100f)

        // Then
        assertEquals(0.0, price, 0.001)
    }

    @Test
    fun `timestampToScreenX should return zero for empty candles`() {
        // Given
        state.candles = emptyList()

        // When
        val x = state.timestampToScreenX(System.currentTimeMillis())

        // Then
        assertEquals(0f, x, 0.001f)
    }

    // ===================================
    // Single Candle 안전성 테스트
    // ===================================

    @Test
    fun `single candle should not crash candleIntervalMs`() {
        // Given - 캔들이 1개만 있는 경우
        state.candles = listOf(
            createTestCandle(timestamp = 1000L)
        )

        // When & Then - candleIntervalMs 계산 시 크래시 없음
        assertEquals(60_000L, state.candleIntervalMs) // default fallback
    }

    @Test
    fun `single candle should return zero visibleTimeRange`() {
        // Given
        state.candles = listOf(
            createTestCandle(timestamp = 1000L)
        )

        // When & Then
        assertEquals(0L, state.visibleTimeRangeMs)
    }

    // ===================================
    // Scroll/Zoom 안전성 테스트
    // ===================================

    @Test
    fun `scroll with empty candles should not crash`() {
        // Given
        state.candles = emptyList()

        // When & Then - no crash
        state.scroll(100f)
        state.scroll(-100f)
    }

    @Test
    fun `zoomAt with empty candles should not crash`() {
        // Given
        state.candles = emptyList()

        // When & Then - no crash
        state.zoomAt(1.5f, 500f)
        state.zoomAt(0.5f, 500f)
    }

    @Test
    fun `isNearStart should be true for empty candles`() {
        // Given
        state.candles = emptyList()

        // When & Then
        assertTrue(state.isNearStart)
    }

    // ===================================
    // Helper
    // ===================================

    private fun createTestCandle(
        timestamp: Long = System.currentTimeMillis(),
        open: Double = 100.0,
        high: Double = 110.0,
        low: Double = 90.0,
        close: Double = 105.0,
        volume: Double = 1000.0
    ) = com.woon.chart.core.model.candle.TradingCandle(
        timestamp = timestamp,
        open = open,
        high = high,
        low = low,
        close = close,
        volume = volume
    )
}
