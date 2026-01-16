package com.woon.domain.ticker.service

import com.woon.domain.ticker.entity.Ticker
import com.woon.domain.ticker.repository.TickerRepository
import com.woon.domain.ticker.usecase.GetTickersUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTickersService
@Inject constructor(
    private val tickerRepository: TickerRepository,
) : GetTickersUseCase {

    override fun invoke(codes: List<String>): Flow<Ticker> {
        return try {
            tickerRepository.observeTickers(codes)
        } catch (e: Exception) {
            throw e
        }
    }
}
