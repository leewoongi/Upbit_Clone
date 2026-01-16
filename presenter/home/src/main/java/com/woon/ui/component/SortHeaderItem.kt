package com.woon.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.woon.core.ui.design.component.text.ChartTextIcon
import com.woon.core.ui.design.theme.color.colorOnSurfaceVariant
import com.woon.core.ui.design.theme.color.colorPrimary
import com.woon.model.constant.SortOrder

@Composable
fun SortHeaderItem(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    sortOrder: SortOrder?,
    onClick: () -> Unit,
    alignment: Alignment = Alignment.CenterStart
) {
    Box(
        modifier = modifier.clickable { onClick() },
        contentAlignment = alignment
    ) {
        ChartTextIcon(
            text = text,
            icon = when (sortOrder) {
                SortOrder.ASC -> Icons.Default.KeyboardArrowUp
                else -> Icons.Default.KeyboardArrowDown
            },
            color = if (isSelected) colorPrimary else colorOnSurfaceVariant
        )
    }
}