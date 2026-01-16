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
import com.woon.ui.component.SortHeaderItem

@Composable
fun SortHeader(
    modifier: Modifier = Modifier,
    currentSortType: SortType,
    currentSortOrder: SortOrder,
    onSortName: () -> Unit,
    onSortPrice: () -> Unit,
    onSortChange: () -> Unit,
    onSortVolume: () -> Unit
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
                isSelected = currentSortType == SortType.NAME,
                sortOrder = if (currentSortType == SortType.NAME) currentSortOrder else null,
                onClick = onSortName
            )
        }

        // 현재가
        SortHeaderItem(
            modifier = Modifier.weight(1f),
            text = SortType.PRICE.label,
            isSelected = currentSortType == SortType.PRICE,
            sortOrder = if (currentSortType == SortType.PRICE) currentSortOrder else null,
            onClick = onSortPrice,
            alignment = Alignment.CenterEnd
        )

        // 전일대비
        SortHeaderItem(
            modifier = Modifier.weight(0.8f),
            text = SortType.CHANGE.label,
            isSelected = currentSortType == SortType.CHANGE,
            sortOrder = if (currentSortType == SortType.CHANGE) currentSortOrder else null,
            onClick = onSortChange,
            alignment = Alignment.CenterEnd
        )

        // 거래대금
        SortHeaderItem(
            modifier = Modifier.weight(1f),
            text = SortType.VOLUME.label,
            isSelected = currentSortType == SortType.VOLUME,
            sortOrder = if (currentSortType == SortType.VOLUME) currentSortOrder else null,
            onClick = onSortVolume,
            alignment = Alignment.CenterEnd
        )
    }
}