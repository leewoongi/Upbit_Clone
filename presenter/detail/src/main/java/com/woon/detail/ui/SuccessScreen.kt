package com.woon.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.woon.core.ui.design.theme.color.colorBackground
import com.woon.core.ui.design.util.ScreenRatios
import com.woon.detail.ui.screen.MarketTopBarScreen
import com.woon.detail.ui.state.DetailUiState

@Composable
fun SuccessScreen(
    modifier: Modifier = Modifier,
    uiState: DetailUiState.Success,
    onZoom: (Float) -> Unit = {},
    onDrag: (Offset) -> Unit = {},
    onSizeChanged: (IntSize) -> Unit = {},
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = colorBackground)
            .padding(16.dp)
    ) {
        MarketTopBarScreen(
            modifier = Modifier
                .fillMaxWidth()
                .weight(ScreenRatios.HEADER_RATIO),
            market = uiState.marketCode,
            price = 20000.toDouble()
        )
    }
}


// 차트
//        Box(modifier = Modifier.weight(0.8f)) {
//            ChartComponent(
//                modifier = Modifier.fillMaxSize(),
//                uiModel = uiState.uiModel,
//                priceRange = uiState.priceRange,
//                onZoom = { onZoom(it) },
//                onDrag = { onDrag(it) },
//                onSizeChanged = { size ->
//                    onSizeChanged(size)
//                }
//            )
//        }
