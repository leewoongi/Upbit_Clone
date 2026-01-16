package com.woon.core.ui.design.component.tab

import androidx.compose.foundation.background
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.woon.core.ui.design.component.text.ChartText
import com.woon.core.ui.design.theme.color.colorOnPrimary
import com.woon.core.ui.design.theme.color.colorOnSurfaceVariant
import com.woon.core.ui.design.theme.color.colorPrimary
import com.woon.core.ui.design.theme.theme.ChartTheme

@Composable
fun ChartTab(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    enabled: Boolean = true,
    selectedBackgroundColor: Color = Color.Transparent,
    onClick: () -> Unit,
) {
    Tab(
        modifier = modifier.background(
            if (selected) selectedBackgroundColor else Color.Transparent
        ),
        selected = selected,
        enabled = enabled,
        onClick = onClick,
        text = {
            ChartText(
                text = text,
                color = when {
                    !enabled -> colorOnSurfaceVariant.copy(alpha = 0.38f)
                    selected && selectedBackgroundColor != Color.Transparent -> colorOnPrimary
                    selected -> colorPrimary
                    else -> colorOnSurfaceVariant
                },
                typography = ChartTheme.typography.bodySmall
            )
        }
    )
}