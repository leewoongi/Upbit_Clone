package com.woon.domain.ticker.usecase

import com.woon.domain.ticker.entity.Ticker
import kotlinx.coroutines.flow.Flow

interface GetTickersUseCase {
    operator fun invoke(codes: List<String>): Flow<Ticker>
}
