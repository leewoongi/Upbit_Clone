package com.woon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woon.domain.event.reporter.ErrorReporter
import com.woon.domain.market.exception.MarketException
import com.woon.domain.market.usecase.GetMarketsUseCase
import com.woon.domain.ticker.exception.TickerException
import com.woon.domain.ticker.usecase.GetTickersUseCase
import kotlinx.coroutines.launch
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
    private val getTickersUseCase: GetTickersUseCase,
    private val errorReporter: ErrorReporter
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
        errorReporter.report(e, SCREEN_NAME)
        emit(
            HomeScreenUiState(
                dataState = HomeDataState.Error(getErrorMessage(e))
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(0),
        initialValue = HomeScreenUiState()
    )

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.Sort -> { onSortClick(intent.type) }
            is HomeIntent.NotificationClick -> { /* TODO */ }
        }
    }

    fun onSortClick(type: SortType) {
        _sortFlow.update { it.toggle(type) }
    }

    private fun getErrorMessage(e: Throwable): String {
        return when (e) {
            is MarketException -> e.message
            is TickerException -> e.message
            else -> "알 수 없는 오류가 발생했습니다"
        }
    }

    companion object {
        private const val SCREEN_NAME = "HomeScreen"
    }
}
