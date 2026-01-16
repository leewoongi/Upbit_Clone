package com.woon.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.woon.core.ui.design.theme.color.colorOutline
import com.woon.model.uimodel.CoinUiModel
import com.woon.ui.component.CoinItem

@Composable
fun CoinList(
    modifier: Modifier = Modifier,
    coins: List<CoinUiModel>,
    onCoinClick: (CoinUiModel) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(
            items = coins,
            key = { it.id }
        ) { coin ->
            CoinItem(
                coin = coin,
                onClick = { onCoinClick(coin) }
            )
            HorizontalDivider(
                color = colorOutline,
                thickness = 0.5.dp
            )
        }
    }
}