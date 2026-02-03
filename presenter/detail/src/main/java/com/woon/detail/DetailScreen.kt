package com.woon.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.woon.chart.component.indicator.bollingerband.model.BollingerBandConfig
import com.woon.chart.component.indicator.crosshair.model.CrosshairConfig
import com.woon.chart.component.indicator.grid.model.GridConfig
import com.woon.chart.component.indicator.ichimokucloud.model.IchimokuConfig
import com.woon.chart.component.indicator.ma.model.MAConfig
import com.woon.chart.core.model.indicator.ma.MAType
import com.woon.chart.ui.TradingChart
import com.woon.chart.ui.theme.chartBollingerLowerColor
import com.woon.chart.ui.theme.chartBollingerMiddleColor
import com.woon.chart.ui.theme.chartBollingerUpperColor
import com.woon.chart.ui.theme.chartCrossHairColor
import com.woon.chart.ui.theme.chartGridColor
import com.woon.chart.ui.theme.chartIchimokuCloudBullishColor
import com.woon.chart.ui.theme.chartIchimokuKijunColor
import com.woon.chart.ui.theme.chartIchimokuTenkanColor
import com.woon.chart.ui.theme.chartMa10Color
import com.woon.chart.ui.theme.chartMa120Color
import com.woon.chart.ui.theme.chartMa200Color
import com.woon.chart.ui.theme.chartMa20Color
import com.woon.chart.ui.theme.chartMa5Color
import com.woon.chart.ui.theme.chartMa60Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import com.woon.core.ui.design.theme.color.colorError
import com.woon.core.ui.design.theme.color.colorPrimary
import com.woon.detail.ui.component.CandleTypeDropdown
import com.woon.detail.ui.intent.DetailIntent
import com.woon.detail.ui.state.DetailUiState
import com.woon.detail.viewmodel.DetailViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp


@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel = hiltViewModel<DetailViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedCandleType by viewModel.candleType.collectAsStateWithLifecycle()

    Column(modifier = modifier) {
        CandleTypeDropdown(
            modifier = Modifier.padding(16.dp),
            selectedType = selectedCandleType,
            onTypeSelected = { viewModel.onIntent(DetailIntent.ChangeTimeFrame(it)) }
        )

        when (val state = uiState) {
            is DetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colorPrimary)
                }
            }

            is DetailUiState.Success -> {
                val candles = state.candles
                if (candles.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "차트 데이터가 없습니다", color = colorPrimary)
                    }
                } else {
                    TradingChart(
                        candles = candles,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        grid = GridConfig.builder()
                            .strokeWidth(0.5f)
                            .lineCount(5)
                            .color(chartGridColor.copy(0.5f)),
                        crosshair = CrosshairConfig.builder()
                            .strokeWidth(1f)
                            .dash(10f, 10f)
                            .color(chartCrossHairColor),
                        bollingerBand = BollingerBandConfig.builder()
                            .enabled(true)
                            .period(20)
                            .multiplier(2f)
                            .upperColor(chartBollingerUpperColor)
                            .middleColor(chartBollingerMiddleColor)
                            .lowerColor(chartBollingerLowerColor)
                            .strokeWidth(1.5f),
                        ichimokuCloud = IchimokuConfig.builder()
                            .enabled(true)
                            .tenkanColor(chartIchimokuTenkanColor)
                            .kijunColor(chartIchimokuKijunColor)
                            .cloudBullishColor(chartIchimokuCloudBullishColor),
                        ma = MAConfig.builder()
                            .enabled(true)
                            .addLine(period = 5, color = chartMa5Color, type = MAType.EMA, strokeWidth = 1f)
                            .addLine(period = 10, color = chartMa10Color, type = MAType.EMA, strokeWidth = 1.5f)
                            .addLine(period = 20, color = chartMa20Color, type = MAType.EMA, strokeWidth = 2f)
                            .addLine(period = 60, color = chartMa60Color, type = MAType.EMA, strokeWidth = 2.5f)
                            .addLine(period = 120, color = chartMa120Color, type = MAType.EMA, strokeWidth = 3f)
                            .addLine(period = 200, color = chartMa200Color, type = MAType.EMA, strokeWidth = 3.5f),
                        onReachStart = { viewModel.onIntent(DetailIntent.LoadCandle) },
                        onZoom = { zoom -> viewModel.onIntent(DetailIntent.ChartZoom(zoom)) },
                        onScroll = { deltaX ->
                            val direction = if (deltaX > 0) "RIGHT" else "LEFT"
                            viewModel.onIntent(DetailIntent.ChartScroll(direction))
                        },
                        onCrosshairToggle = { enabled ->
                            viewModel.onIntent(DetailIntent.CrosshairToggle(enabled))
                        }
                    )
                }
            }

            is DetailUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        // 에러 메시지
                        Text(
                            text = state.message,
                            color = Color(0xFFB0B0B0),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )

                        // 자동 재시도 중 표시
                        if (state.isAutoRetrying) {
                            Spacer(modifier = Modifier.height(8.dp))
                            CircularProgressIndicator(
                                modifier = Modifier.height(24.dp),
                                color = colorPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "재시도 중... (${state.retryCount}/2)",
                                color = Color(0xFF808080),
                                fontSize = 12.sp
                            )
                        }

                        // 재시도 버튼 (자동 재시도 중이 아닐 때만 표시)
                        if (state.canRetry && !state.isAutoRetrying) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.retry() }
                            ) {
                                Text(text = state.retryButtonText)
                            }
                        }
                    }
                }
            }

            is DetailUiState.Empty -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "데이터가 없습니다", color = colorPrimary)
                }
            }
        }
    }
}
