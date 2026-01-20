package com.woon.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.woon.core.ui.design.theme.color.colorSurfaceVariant
import com.woon.model.constant.SortOrder
import com.woon.model.constant.SortType
import com.woon.model.uimodel.SortUiState
import com.woon.ui.component.SortHeaderItem

@Composable
fun SortHeader(
    modifier: Modifier = Modifier,
    sortState: SortUiState,
    onSortClick: (SortType) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colorSurfaceVariant)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1.2f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.size(width = 32.dp, height = 16.dp))

            SortHeaderItem(
                text = SortType.NAME.label,
                isSelected = sortState.isSelected(SortType.NAME),
                sortOrder = sortState.orderFor(SortType.NAME),
                onClick = { onSortClick(SortType.NAME) }
            )
        }

        SortHeaderItem(
            modifier = Modifier.weight(1f),
            text = SortType.PRICE.label,
            isSelected = sortState.isSelected(SortType.PRICE),
            sortOrder = sortState.orderFor(SortType.PRICE),
            onClick = { onSortClick(SortType.PRICE) },
            alignment = Alignment.CenterEnd
        )

        SortHeaderItem(
            modifier = Modifier.weight(0.8f),
            text = SortType.CHANGE.label,
            isSelected = sortState.isSelected(SortType.CHANGE),
            sortOrder = sortState.orderFor(SortType.CHANGE),
            onClick = { onSortClick(SortType.CHANGE) },
            alignment = Alignment.CenterEnd
        )

        SortHeaderItem(
            modifier = Modifier.weight(1f),
            text = SortType.VOLUME.label,
            isSelected = sortState.isSelected(SortType.VOLUME),
            sortOrder = sortState.orderFor(SortType.VOLUME),
            onClick = { onSortClick(SortType.VOLUME) },
            alignment = Alignment.CenterEnd
        )
    }
}