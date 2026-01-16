package com.woon.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.woon.core.ui.design.component.tab.ChartTab
import com.woon.core.ui.design.component.tab.ChartTabRow
import com.woon.core.ui.design.component.tab.TabDividerOrientation
import com.woon.core.ui.design.theme.color.colorOutline
import com.woon.core.ui.design.theme.color.colorPrimary
import com.woon.model.constant.MarketType

@Composable
fun MarketTabRow(
    modifier: Modifier = Modifier,
    selectedMarket: MarketType,
    onMarketSelected: (MarketType) -> Unit
) {
    val items = listOf(MarketType.KRW, MarketType.BTC, MarketType.USDT, MarketType.FAVORITE)
    val selectedIndex = items.indexOf(selectedMarket)

    ChartTabRow(
        modifier = modifier
            .border(
                width = 1.dp,
                color = colorOutline,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp)),
        selectedTabIndex = selectedIndex,
        containerColor = Color.Transparent,
        indicator = {},
        dividerOrientation = TabDividerOrientation.VERTICAL,
        divider = { VerticalDivider(color = colorOutline) }
    ) {
        items.forEach { market ->
            val isSelected = selectedMarket == market

            ChartTab(
                text = market.label,
                selected = isSelected,
                selectedBackgroundColor = colorPrimary,
                onClick = { onMarketSelected(market) }
            )
        }
    }
}