package com.woon.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.woon.core.ui.design.component.canvas.ChartCanvas
import com.woon.core.ui.design.component.text.ChartText
import com.woon.core.ui.design.theme.color.colorError
import com.woon.core.ui.design.theme.color.colorOnSurface
import com.woon.core.ui.design.theme.color.colorOnSurfaceVariant
import com.woon.core.ui.design.theme.color.colorPrimary
import com.woon.core.ui.design.theme.theme.ChartTheme
import com.woon.domain.ticker.entity.constant.ChangeType
import com.woon.model.uimodel.CoinUiModel

@Composable
fun CoinItem(
    modifier: Modifier = Modifier,
    coin: CoinUiModel,
    onClick: () -> Unit
) {
    val changeColor = when (coin.changeType) {
        ChangeType.EVEN -> colorOnSurfaceVariant
        ChangeType.RISE -> colorError
        ChangeType.FALL -> colorPrimary
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 열1: 미니차트 + 코인명
        Row(
            modifier = Modifier.weight(1.2f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 미니차트
            ChartCanvas(
                modifier = Modifier.size(width = 32.dp, height = 16.dp),
                data = emptyList(),
                lineColor = changeColor
            )

            // 코인명/심볼
            Column {
                ChartText(
                    text = coin.name,
                    color = colorOnSurface,
                    typography = ChartTheme.typography.bodyMedium
                )
                ChartText(
                    text = coin.symbol,
                    color = colorOnSurfaceVariant,
                    typography = ChartTheme.typography.labelSmall
                )
            }
        }

        ChartText(
            modifier = Modifier.weight(1f),
            text = coin.price,
            color = changeColor,
            typography = ChartTheme.typography.bodyMedium,
            textAlign = TextAlign.End
        )

        // 열3: 변동률
        ChartText(
            modifier = Modifier.weight(0.8f),
            text = coin.changeRate,
            color = changeColor,
            typography = ChartTheme.typography.bodyMedium,
            textAlign = TextAlign.End
        )

        // 열4: 거래대금
        ChartText(
            modifier = Modifier.weight(1f),
            text = coin.volume,
            color = colorOnSurfaceVariant,
            typography = ChartTheme.typography.bodySmall,
            textAlign = TextAlign.End
        )
    }
}