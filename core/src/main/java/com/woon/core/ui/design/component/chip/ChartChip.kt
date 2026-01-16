package com.woon.core.ui.design.component.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.woon.core.ui.design.component.text.ChartText
import com.woon.core.ui.design.theme.color.colorOutline
import com.woon.core.ui.design.theme.theme.ChartTheme

@Composable
fun ChartChip(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    enabled: Boolean = true,
    style: ChartChipStyle = ChartChipStyle.FILLED,
    onClick: () -> Unit,
){

    val borderModifier = if (style == ChartChipStyle.OUTLINED) {
        modifier.border(
            width = 1.dp,
            color = when {
                !enabled -> colorOutline.copy(alpha = 0.38f)
                selected -> Color.Transparent
                else -> colorOutline
            },
            shape = RoundedCornerShape(50)
        )
    } else {
        modifier
    }

    Box(
        modifier = borderModifier
            .clip(RoundedCornerShape(50))
            .background(
                if (selected) ChartTheme.colors.primary else Color.Transparent
            )
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        ChartText(
            text = text,
            color = when {
                !enabled -> ChartTheme.colors.onSurfaceVariant.copy(alpha = 0.38f)
                selected -> ChartTheme.colors.onPrimary
                else -> ChartTheme.colors.onSurfaceVariant
            },
            typography = if (selected) {
                ChartTheme.typography.titleMedium
            } else {
                ChartTheme.typography.bodyMedium
            }
        )
    }
}