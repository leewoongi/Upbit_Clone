package com.woon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woon.domain.market.usecase.GetMarketsUseCase
import com.woon.domain.ticker.usecase.GetTickersUseCase
import com.woon.ext.sortedBy
import com.woon.model.constant.SortType
import com.woon.model.mapper.toCoinUiModel
import com.woon.model.uimodel.CoinUiModel
import com.woon.model.uimodel.SortUiState
import com.woon.model.uistate.HomeDataState
import com.woon.model.uistate.HomeScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMarketsUseCase: GetMarketsUseCase,
    private val getTickersUseCase: GetTickersUseCase
) : ViewModel() {

    private val _sortFlow = MutableStateFlow(SortUiState())

    private val _tickerFlow: Flow<Map<String, CoinUiModel>> = flow {
        val markets = getMarketsUseCase.getKrwMarkets()
        val codes = markets.map { it.code }

        val map = mutableMapOf<String, CoinUiModel>()

        getTickersUseCase(codes).collect { ticker ->
            val market = markets.find { it.code == ticker.code }
            map[ticker.code] = ticker.toCoinUiModel(market)
            emit(map.toMap())
        }
    }

    val uiState: StateFlow<HomeScreenUiState> = combine(
        _tickerFlow,
        _sortFlow
    ) { coins, sort ->
        HomeScreenUiState(
            dataState = HomeDataState.Success(
                coins.values.toList().
                sortedBy(sort)
            ),
            sort = sort
        )
    }.catch { e ->
        emit(
            HomeScreenUiState(
                dataState = HomeDataState.Error(e.message ?: "Error")
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(0),  // 👈 구독 끊기면 즉시 중지
        initialValue = HomeScreenUiState()
    )

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.Sort -> {
                onSortClick(intent.type)
            }
        }
    }

    fun onSortClick(type: SortType) {
        _sortFlow.update { it.toggle(type) }
    }
}
