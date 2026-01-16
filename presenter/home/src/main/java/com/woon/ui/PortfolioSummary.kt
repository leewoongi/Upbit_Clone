package com.woon.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.woon.ui.component.LabeledValue
import com.woon.core.ui.design.theme.color.colorOutline

// 목 데이터로 대체
@Composable
fun PortfolioSummary(
    modifier: Modifier = Modifier,
    totalPurchase: String,
    totalValue: String,
    profitLoss: String,
    profitLossColor: Color,
    profitRate: String,
    profitRateColor: Color
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colorOutline)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            LabeledValue(
                modifier = Modifier.weight(1f),
                label = "총 매수",
                value = totalPurchase
            )

            LabeledValue(
                modifier = Modifier.weight(1f),
                label = "평가손익",
                value = profitLoss,
                valueColor = profitLossColor
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            LabeledValue(
                modifier = Modifier.weight(1f),
                label = "총 평가",
                value = totalValue
            )
            LabeledValue(
                modifier = Modifier.weight(1f),
                label = "수익률",
                value = profitRate,
                valueColor = profitRateColor
            )
        }
    }
}