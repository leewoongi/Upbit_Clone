package com.woon.chart.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.woon.chart.component.PriceScale
import com.woon.chart.component.TimeScale
import com.woon.chart.component.indicator.bollingerband.drawBollingerBand
import com.woon.chart.component.indicator.bollingerband.model.BollingerBandConfig
import com.woon.chart.component.indicator.crosshair.drawCrossHair
import com.woon.chart.component.indicator.crosshair.model.CrosshairConfig
import com.woon.chart.component.indicator.grid.drawGrid
import com.woon.chart.component.indicator.grid.model.GridConfig
import com.woon.chart.component.indicator.ichimokucloud.drawIchimokuCloud
import com.woon.chart.component.indicator.ichimokucloud.model.IchimokuConfig
import com.woon.chart.component.indicator.ma.drawMovingAverage
import com.woon.chart.component.indicator.ma.model.MAConfig
import com.woon.chart.component.indicator.volume.model.VolumeConfig
import com.woon.chart.core.model.candle.TradingCandle
import com.woon.chart.ui.theme.chartBackgroundColor
import com.woon.chart.ui.theme.chartFallingColor
import com.woon.chart.ui.theme.chartRisingColor

@Composable
fun TradingChart(
    candles: List<TradingCandle>,
    modifier: Modifier = Modifier,
    state: TradingChartState = rememberTradingCanvasState(),
    grid: GridConfig.Builder = GridConfig.builder(),
    crosshair: CrosshairConfig.Builder = CrosshairConfig.builder(),
    bollingerBand: BollingerBandConfig.Builder = BollingerBandConfig.builder(),
    ichimokuCloud: IchimokuConfig.Builder = IchimokuConfig.builder(),
    ma: MAConfig.Builder = MAConfig.builder(),
    volume: VolumeConfig.Builder = VolumeConfig.builder(),
    strokeWidth: Float = 2f,
    minBodyHeight: Float = 1f,
    risingColor: Color = chartRisingColor,
    fallingColor: Color = chartFallingColor,
    priceScaleWidth: Dp = 60.dp,
    timeScaleHeight: Dp = 24.dp,
    onReachStart: () -> Unit = {}
) {
    SideEffect {
        state.indicatorState.update(grid.build())
        state.indicatorState.update(crosshair.build())
        state.indicatorState.update(bollingerBand.candles(candles).build())
        state.indicatorState.update(ichimokuCloud.candles(candles).build())
        state.indicatorState.update(ma.candles(candles).build())
        state.indicatorState.update(volume.build())
    }

    Column(
        modifier = modifier.background(chartBackgroundColor)
    ) {
        Row(modifier = Modifier.weight(1f)) {
            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp)
            ) {
                val density = LocalDensity.current
                val width = with(density) { maxWidth.toPx() }
                val height = with(density) { maxHeight.toPx() }

                state.candles = candles
                state.screenWidth = width
                state.screenHeight = height

                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { offset ->
                                    state.indicatorState.toggleCrosshair(offset.x, offset.y)
                                }
                            )
                        }
                        .pointerInput(Unit) {
                            detectTransformGestures { centroid, pan, zoom, _ ->
                                if (zoom != 1f) {
                                    state.zoomAt(zoom, centroid.x)
                                } else if (pan != Offset.Zero) {
                                    if (state.indicatorState.crosshairConfig.model.enabled) {
                                        state.indicatorState.moveCrosshair(centroid.x, centroid.y)
                                    } else {
                                        state.scroll(pan.x)
                                        if (state.isNearStart) onReachStart()
                                    }
                                }
                            }
                        }
                ) {
                    drawGrid(state, state.indicatorState.gridConfig)

                    if (state.indicatorState.bollingerBandConfig.enabled) {
                        drawBollingerBand(state, state.indicatorState.bollingerBandConfig)
                    }

                    if (state.indicatorState.maConfig.enabled) {
                        drawMovingAverage(state, state.indicatorState.maConfig)
                    }

                    if(state.indicatorState.ichimokuConfig.enabled) {
                        drawIchimokuCloud(state, state.indicatorState.ichimokuConfig)
                    }

                    // 캔들
                    state.visibleCandles.forEachIndexed { i, candle ->
                        val index = state.visibleStartIndex + i
                        val color = if (candle.isRising) risingColor else fallingColor

                        drawLine(
                            color = color,
                            start = Offset(state.indexToScreenX(index), state.priceToY(candle.high)),
                            end = Offset(state.indexToScreenX(index), state.priceToY(candle.low)),
                            strokeWidth = strokeWidth
                        )

                        val bodyTop = state.priceToY(maxOf(candle.open, candle.close))
                        val bodyBottom = state.priceToY(minOf(candle.open, candle.close))

                        drawRect(
                            color = color,
                            topLeft = Offset(state.indexToScreenX(index) - state.candleBodyWidth / 2, bodyTop),
                            size = Size(state.candleBodyWidth, (bodyBottom - bodyTop).coerceAtLeast(minBodyHeight))
                        )
                    }

                    if (state.indicatorState.crosshairConfig.model.enabled) {
                        drawCrossHair(state.indicatorState.crosshairConfig)
                    }
                }
            }

            PriceScale(
                state = state,
                modifier = Modifier
                    .width(priceScaleWidth)
                    .fillMaxHeight()
            )
        }

        TimeScale(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .height(timeScaleHeight)
        )
    }
}