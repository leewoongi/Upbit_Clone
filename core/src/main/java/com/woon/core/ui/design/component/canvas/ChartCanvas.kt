package com.woon.core.ui.design.component.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.woon.core.ui.design.theme.color.colorPrimary

@Composable
fun ChartCanvas(
    modifier: Modifier = Modifier,
    data: List<Float>,
    lineColor: Color = colorPrimary,
    lineWidth: Dp = 1.dp
) {
    Canvas(modifier = modifier) {
        // 라인 그리기
    }
}