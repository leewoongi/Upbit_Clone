package com.woon.detail.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.woon.core.ui.design.component.text.ChartText
import com.woon.core.ui.design.theme.theme.ChartTheme

/**
 * 뒤로가기 버튼, Market Id, 설정 아이콘,
 */
@Composable
fun MarketTopBarScreen(
    modifier: Modifier = Modifier,
    market: String,
    price: Double
) {
    Row(
        modifier = modifier
    ){
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
        )

        ChartText(
            text = market,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = ChartTheme.colors.primary,
            typography = MaterialTheme.typography.headlineSmall
        )

        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "More",
            modifier = Modifier.size(24.dp)
        )
    }

    Column(

    ) {

    }

}