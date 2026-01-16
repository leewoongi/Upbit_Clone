package com.woon.detail.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import com.woon.detail.extension.drag
import com.woon.detail.extension.drawCandle
import com.woon.detail.extension.pinchZoom
import com.woon.detail.model.CandleUiModel

// 차트를 그리는 컴포넌트
// 1. 캔들
@Composable
fun ChartComponent(
    modifier: Modifier = Modifier,
    uiModel: List<CandleUiModel>,
    priceRange: Pair<Double, Double>,
    onZoom: (Float) -> Unit = {},
    onDrag: (Offset) -> Unit = {},
    onSizeChanged: (IntSize) -> Unit = {},
) {

    var zoom by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Canvas(
        modifier
            .background(color = Color.White)
            .pinchZoom(
                getZoom = { zoom },
                setZoom = {
                    zoom = it
                },
                getOffset = { offset },
                setOffset = {
                    offset = it
                },
                pinchZoom = { onZoom(zoom) }
            )
            .drag(
                getOffset = { offset },
                setOffset = {
                    offset = it
                },
                drag = { onDrag(offset) }
            )
            .onSizeChanged { size ->
                onSizeChanged(size)
            }
    ) {
        // 줌/오프셋을 그리기 변환에 적용
        withTransform({
            translate(offset.x, offset.y)
            scale(zoom, zoom)
        }) {
            uiModel.forEachIndexed { index, candle ->
                drawCandle(
                    uiModel = candle,
                    priceRange = priceRange,
                    positionX = candle.getPositionX(index = index)
                )
            }
        }
    }
}