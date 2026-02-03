package com.woon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.woon.core.ui.design.theme.color.colorBackground
import com.woon.core.ui.design.theme.color.colorError
import com.woon.core.ui.design.theme.color.colorOutline
import com.woon.core.ui.design.theme.color.colorPrimary
import com.woon.model.constant.ChartChipType
import com.woon.model.constant.MarketType
import com.woon.model.uistate.HomeDataState
import com.woon.ui.CoinList
import com.woon.ui.MarketTabRow
import com.woon.ui.PortfolioSummary
import com.woon.ui.SearchBar
import com.woon.ui.SortHeader
import com.woon.ui.TopBar
import com.woon.core.ui.provides.LocalNavController
import com.woon.viewmodel.HomeIntent
import com.woon.viewmodel.HomeViewModel

@Composable
fun HomeScreen() {
    val viewModel = hiltViewModel<HomeViewModel>()
    val navController = LocalNavController.current

    var selectedType by rememberSaveable { mutableStateOf(ChartChipType.EXCHANGE) }
    var query by rememberSaveable { mutableStateOf("") }
    var selectedMarket by rememberSaveable { mutableStateOf(MarketType.FAVORITE) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorBackground)
            .statusBarsPadding()
    ) {
        TopBar(
            modifier = Modifier.fillMaxWidth(),
            selectedType = selectedType,
            onTabSelected = { type ->
                viewModel.recordClick("TopTab", mapOf("tab" to type.name))
                selectedType = type
            },
            onNotificationClick = { viewModel.onIntent(HomeIntent.NotificationClick) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            query = query,
            onQueryChange = { query = it },
            onSearch = { }
        )

        HorizontalDivider(
            color = colorPrimary,
            thickness = 2.dp
        )

        PortfolioSummary(
            modifier = Modifier.fillMaxWidth(),
            totalPurchase = "59,042,845",
            totalValue = "38,431,373",
            profitLoss = "-20,611,470",
            profitLossColor = colorError,
            profitRate = "-34.91%",
            profitRateColor = colorError
        )

        MarketTabRow(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            selectedMarket = selectedMarket,
            onMarketSelected = { market ->
                viewModel.recordClick("MarketTab", mapOf("market" to market.name))
                selectedMarket = market
            }
        )

        HorizontalDivider(
            color = colorOutline,
            thickness = 2.dp
        )

        SortHeader(
            sortState = uiState.sort,
            onSortClick = { type -> viewModel.onIntent(HomeIntent.Sort(type)) }
        )

        when (val dataState = uiState.dataState) {
            is HomeDataState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colorPrimary)
                }
            }

            is HomeDataState.Success -> {
                CoinList(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    coins = dataState.coins,
                    onCoinClick = { coin ->
                        viewModel.recordClick("CoinItem", mapOf("symbol" to coin.symbol))
                        viewModel.recordNav("detail/${coin.id}")
                        navController.navigate("detail/${coin.id}")
                    }
                )
            }

            is HomeDataState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dataState.message,
                        color = colorError
                    )
                }
            }

            is HomeDataState.Empty -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "데이터가 없습니다",
                        color = colorPrimary
                    )
                }
            }
        }
    }
}