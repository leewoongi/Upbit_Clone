package com.woon.chart.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.woon.chart.core.model.candle.TradingCandle
import com.woon.chart.ui.extension.toCleanTimeInterval
import com.woon.chart.ui.indicator.IndicatorState

class TradingChartState(
    val visibleCount: Int,
    val candleSpacing: Float,
    val minVisibleCount: Int,
    val maxVisibleCount: Int,
    val startPadding: Float = 0.1f
) {
    // ========================================
    // 외부에서 설정 (변경 체크)
    // ========================================

    private var _candles by mutableStateOf<List<TradingCandle>>(emptyList())
    var candles: List<TradingCandle>
        get() = _candles
        set(value) {
            if (_candles !== value) {
                _candles = value
            }
        }

    private var _screenWidth by mutableFloatStateOf(0f)
    var screenWidth: Float
        get() = _screenWidth
        set(value) {
            if (_screenWidth != value) {
                _screenWidth = value
            }
        }

    private var _screenHeight by mutableFloatStateOf(0f)
    var screenHeight: Float
        get() = _screenHeight
        set(value) {
            if (_screenHeight != value) {
                _screenHeight = value
            }
        }

    // ========================================
    // 내부 상태
    // ========================================

    var scrollOffset by mutableFloatStateOf(0f)
        private set
    var scale by mutableFloatStateOf(1f)
        private set

    // ========================================
    // 줌 제한
    // ========================================

    private val minScale: Float
        get() = visibleCount.toFloat() / maxVisibleCount

    private val maxScale: Float
        get() = visibleCount.toFloat() / minVisibleCount

    // ========================================
    // 캔들 크기
    // ========================================

    val candleWidth: Float by derivedStateOf {
        if (_screenWidth > 0f) _screenWidth / visibleCount else 50f
    }

    val candleSlot: Float by derivedStateOf {
        candleWidth * scale
    }

    val candleBodyWidth: Float by derivedStateOf {
        candleSlot * (1 - candleSpacing)
    }

    // ========================================
    // 캔들 타임프레임 (자동 감지)
    // ========================================

    val candleIntervalMs: Long by derivedStateOf {
        if (_candles.size < 2) return@derivedStateOf 60_000L
        _candles[1].timestamp - _candles[0].timestamp
    }

    val candleMinutes: Int by derivedStateOf {
        (candleIntervalMs / 60_000L).toInt().coerceAtLeast(1)
    }

    // ========================================
    // 카메라 위치
    // ========================================

    private val baseOffsetX: Float by derivedStateOf {
        if (_candles.isEmpty() || _screenWidth == 0f) return@derivedStateOf 0f

        val padding = _screenWidth * startPadding
        val slot = candleSlot
        val actualVisibleCount = (_screenWidth / slot).toInt()
        if (_candles.size <= actualVisibleCount) {
            return@derivedStateOf padding
        }
        val targetIndex = (_candles.size - actualVisibleCount).coerceAtLeast(0)
        -targetIndex * candleWidth * scale
    }

    val offsetX: Float by derivedStateOf {
        baseOffsetX + scrollOffset
    }

    // ========================================
    // 좌표 변환
    // ========================================

    private fun toScreenX(dataX: Float): Float {
        return (dataX * scale) + offsetX
    }

    private fun toDataX(screenX: Float): Float {
        return (screenX - offsetX) / scale
    }

    // ========================================
    // 보이는 범위 (캐싱)
    // ========================================

    val visibleStartIndex: Int by derivedStateOf {
        val offset = offsetX
        val currentScale = scale
        val width = candleWidth
        val dataX = (0f - offset) / currentScale
        (dataX / width).toInt().coerceAtLeast(0)
    }

    val visibleEndIndex: Int by derivedStateOf {
        val offset = offsetX
        val currentScale = scale
        val width = candleWidth
        val dataX = (_screenWidth - offset) / currentScale
        (dataX / width).toInt().coerceAtMost(_candles.size)
    }

    val visibleCandles: List<TradingCandle> by derivedStateOf {
        if (_candles.isEmpty() || visibleStartIndex >= visibleEndIndex) {
            emptyList()
        } else {
            _candles.subList(visibleStartIndex, visibleEndIndex)
        }
    }

    // ========================================
    // 인디케이터 상태
    // ========================================

    val indicatorState = IndicatorState(
        getVisibleStartIndex = { visibleStartIndex },
        getVisibleCount = { visibleCandles.size }
    )

    // ========================================
    // 가격 범위 (캐싱)
    // ========================================

    val minPrice: Double by derivedStateOf {
        val candleMin = visibleCandles.minOfOrNull { it.low } ?: 0.0
        val indicatorMin = indicatorState.priceRange?.first ?: candleMin
        minOf(candleMin, indicatorMin)
    }

    val maxPrice: Double by derivedStateOf {
        val candleMax = visibleCandles.maxOfOrNull { it.high } ?: 0.0
        val indicatorMax = indicatorState.priceRange?.second ?: candleMax
        maxOf(candleMax, indicatorMax)
    }

    val priceRange: Double by derivedStateOf {
        maxPrice - minPrice
    }

    // ========================================
    // 위치 계산
    // ========================================

    fun indexToScreenX(index: Int): Float {
        val dataX = index * candleWidth + candleWidth / 2
        return toScreenX(dataX)
    }

    fun priceToY(price: Double): Float {
        if (priceRange == 0.0) return _screenHeight / 2
        return (_screenHeight * (1 - (price - minPrice) / priceRange)).toFloat()
    }

    fun yToPrice(y: Float): Double {
        if (_screenHeight == 0f) return 0.0
        return minPrice + priceRange * (1 - y / _screenHeight)
    }

    // ========================================
    // 시간 격자 계산
    // ========================================

    val visibleTimeRangeMs: Long by derivedStateOf {
        val first = visibleCandles.firstOrNull() ?: return@derivedStateOf 0L
        val last = visibleCandles.lastOrNull() ?: return@derivedStateOf 0L
        if (visibleCandles.size < 2) return@derivedStateOf 0L
        last.timestamp - first.timestamp
    }

    val gridIntervalMs: Long by derivedStateOf {
        if (visibleTimeRangeMs <= 0) return@derivedStateOf candleIntervalMs

        val slot = candleSlot
        val actualVisibleCount = (_screenWidth / slot).toInt()

        val effectiveTimeRange = if (_candles.size < actualVisibleCount) {
            actualVisibleCount.toLong() * candleIntervalMs
        } else {
            visibleTimeRangeMs
        }
        val idealMs = effectiveTimeRange / 5
        val calculated = idealMs.toCleanTimeInterval().coerceAtLeast(candleIntervalMs)
        calculated.coerceAtMost(visibleTimeRangeMs)
    }

    fun timestampToScreenX(timestamp: Long): Float {
        if (_candles.isEmpty()) return 0f

        for (i in 0 until _candles.size - 1) {
            val current = _candles[i]
            val next = _candles[i + 1]

            if (timestamp >= current.timestamp && timestamp < next.timestamp) {
                val ratio = (timestamp - current.timestamp).toFloat() /
                        (next.timestamp - current.timestamp).toFloat()
                return indexToScreenX(i) + (indexToScreenX(i + 1) - indexToScreenX(i)) * ratio
            }
        }

        val firstCandle = _candles.firstOrNull() ?: return 0f
        return if (timestamp < firstCandle.timestamp) {
            indexToScreenX(0)
        } else {
            indexToScreenX(_candles.size - 1)
        }
    }

    // ========================================
    // 스크롤
    // ========================================

    private fun canScroll(deltaX: Float): Boolean {
        return when {
            deltaX > 0 -> visibleStartIndex > 0
            deltaX < 0 -> visibleEndIndex < _candles.size
            else -> true
        }
    }

    fun scroll(deltaX: Float) {
        if (canScroll(deltaX)) {
            scrollOffset += deltaX
        }
    }

    val isNearStart: Boolean by derivedStateOf {
        visibleStartIndex <= 20
    }

    // ========================================
    // 줌
    // ========================================

    fun zoomAt(zoomFactor: Float, focusX: Float) {
        val newScale = (scale * zoomFactor).coerceIn(minScale, maxScale)
        if (newScale == scale) return

        val dataX = toDataX(focusX)
        scale = newScale
        val newOffsetX = focusX - (dataX * newScale)
        scrollOffset = newOffsetX - baseOffsetX
    }
}

@Composable
fun rememberTradingCanvasState(
    visibleCount: Int = 100,
    candleSpacing: Float = 0.2f,
    minVisibleCount: Int = 24,
    maxVisibleCount: Int = 120,
    startPadding: Float = 0.1f
): TradingChartState {
    return remember {
        TradingChartState(
            visibleCount = visibleCount,
            candleSpacing = candleSpacing,
            minVisibleCount = minVisibleCount,
            maxVisibleCount = maxVisibleCount,
            startPadding = startPadding
        )
    }
}