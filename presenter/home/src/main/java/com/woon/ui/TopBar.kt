package com.woon.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.woon.core.ui.design.component.chip.ChartChip
import com.woon.core.ui.design.theme.theme.ChartTheme
import com.woon.model.constant.ChartChipType

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    selectedType: ChartChipType,
    onTabSelected: (ChartChipType) -> Unit,
){
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChartChipType.entries.forEach { type ->
                ChartChip(
                    text = type.label,
                    selected = type == selectedType,
                    enabled = type.enabled,
                    onClick = { onTabSelected(type) }
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "Settings",
                modifier = Modifier.size(24.dp),
                tint = ChartTheme.colors.onBackground
            )

            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notifications",
                modifier = Modifier.size(24.dp),
                tint = ChartTheme.colors.onBackground
            )
        }
    }
}