package com.woon.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woon.domain.market.entity.Market
import com.woon.domain.market.usecase.GetMarketsUseCase
import com.woon.domain.ticker.usecase.GetTickersUseCase
import com.woon.model.constant.SortOrder
import com.woon.model.constant.SortType
import com.woon.model.mapper.toCoinUiModel
import com.woon.model.uimodel.CoinUiModel
import com.woon.model.uistate.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMarketsUseCase: GetMarketsUseCase,
    private val getTickersUseCase: GetTickersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var coinMap = emptyMap<String, CoinUiModel>()
    private var currentSortType = SortType.VOLUME
    private var currentSortOrder = SortOrder.DESC

    init {
        loadMarketsAndObserveTickers()
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.ChangeSortName -> handleSortName()
            HomeIntent.ChangeSortPrice -> handleSortPrice()
            HomeIntent.ChangeSortChange -> handleSortChange()
            HomeIntent.ChangeSortVolume -> handleSortVolume()
        }
    }

    private fun loadMarketsAndObserveTickers() {
        viewModelScope.launch {
            runCatching {
                getMarketsUseCase.getKrwMarkets()
            }.onSuccess { markets ->
                observeTickers(markets)
            }.onFailure { e ->
                Log.e("HomeViewModel", "Failed to load markets: ${e.message}", e)
                _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun observeTickers(markets: List<Market>) {
        viewModelScope.launch {
            val codes = markets.map { it.code }
            getTickersUseCase(codes)
                .runningFold(emptyMap<String, CoinUiModel>()) { map, ticker ->
                    val market = markets.find { it.code == ticker.code }
                    map + (ticker.code to ticker.toCoinUiModel(market))
                }
                .catch { e ->
                    _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
                }
                .collect { map ->
                    coinMap = map
                    updateUiState()
                }
        }
    }

    private fun handleSortName() {
        if (currentSortType == SortType.NAME) {
            currentSortOrder = toggleSortOrder()
        } else {
            currentSortType = SortType.NAME
            currentSortOrder = SortOrder.DESC
        }
        updateUiState()
    }

    private fun handleSortPrice() {
        if (currentSortType == SortType.PRICE) {
            currentSortOrder = toggleSortOrder()
        } else {
            currentSortType = SortType.PRICE
            currentSortOrder = SortOrder.DESC
        }
        updateUiState()
    }

    private fun handleSortChange() {
        if (currentSortType == SortType.CHANGE) {
            currentSortOrder = toggleSortOrder()
        } else {
            currentSortType = SortType.CHANGE
            currentSortOrder = SortOrder.DESC
        }
        updateUiState()
    }

    private fun handleSortVolume() {
        if (currentSortType == SortType.VOLUME) {
            currentSortOrder = toggleSortOrder()
        } else {
            currentSortType = SortType.VOLUME
            currentSortOrder = SortOrder.DESC
        }
        updateUiState()
    }

    private fun toggleSortOrder(): SortOrder {
        return if (currentSortOrder == SortOrder.ASC) SortOrder.DESC else SortOrder.ASC
    }

    private fun updateUiState() {
        val sortedCoins = sortCoins(coinMap.values.toList())
        _uiState.value = HomeUiState.Success(
            coins = sortedCoins,
            sortType = currentSortType,
            sortOrder = currentSortOrder
        )
    }

    private fun sortCoins(coins: List<CoinUiModel>): List<CoinUiModel> {
        val comparator: Comparator<CoinUiModel> = when (currentSortType) {
            SortType.NAME -> compareBy { it.name }
            SortType.PRICE -> compareBy { it.rawPrice }
            SortType.CHANGE -> compareBy { it.rawChangeRate }
            SortType.VOLUME -> compareBy { it.rawVolume }
        }

        return if (currentSortOrder == SortOrder.ASC) {
            coins.sortedWith(comparator)
        } else {
            coins.sortedWith(comparator.reversed())
        }
    }
}
