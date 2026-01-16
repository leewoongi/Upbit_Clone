package com.woon.core.ui.design.component.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.woon.core.ui.design.theme.color.colorOnSurfaceVariant
import com.woon.core.ui.design.theme.theme.ChartTheme

@Composable
fun ChartTextIcon(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    color: Color = colorOnSurfaceVariant,
    typography: TextStyle = ChartTheme.typography.labelSmall,
    iconSize: Dp = 14.dp
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        ChartText(
            text = text,
            color = color,
            typography = typography
        )

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(iconSize),
            tint = color
        )
    }
}