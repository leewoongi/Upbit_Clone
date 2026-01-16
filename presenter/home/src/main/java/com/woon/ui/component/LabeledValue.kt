package com.woon.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.woon.core.ui.design.component.text.ChartText
import com.woon.core.ui.design.theme.color.colorError
import com.woon.core.ui.design.theme.color.colorOnSurfaceVariant
import com.woon.core.ui.design.theme.theme.ChartTheme

@Composable
fun LabeledValue(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    valueColor: Color = colorError
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ChartText(
            text = label,
            color = colorOnSurfaceVariant,
            typography = ChartTheme.typography.labelLarge
        )

        ChartText(
            text = value,
            color = valueColor,
            typography = ChartTheme.typography.labelLarge
        )
    }
}