package com.woon.detail

import androidx.compose.foundation.layout.Column
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
import com.woon.detail.ui.component.CandleTypeDropdown
import com.woon.detail.ui.intent.DetailIntent
import com.woon.detail.ui.state.DetailUiState
import com.woon.detail.viewmodel.DetailViewModel


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

        when (uiState) {
            is DetailUiState.Loading -> {}
            is DetailUiState.Success -> {
                val candles = (uiState as DetailUiState.Success).candles
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
                    onReachStart = { viewModel.onIntent(DetailIntent.LoadCandle) }
                )
            }
            is DetailUiState.Error -> {}
        }
    }
}
