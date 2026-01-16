package com.woon.repository.candle

import com.woon.datasource.remote.http.candle.RemoteCandleDataSource
import com.woon.domain.candle.entity.Candle
import com.woon.domain.candle.repository.CandleRepository
import com.woon.repository.candle.mapper.toDomain
import javax.inject.Inject

class GetCandleRepositoryImpl
@Inject constructor(
    private val remoteCandleDataSource: RemoteCandleDataSource
): CandleRepository {

    override suspend fun getCandle(unit: Int): List<Candle> {
        return remoteCandleDataSource.getCandle(240).map {
            it.toDomain()
        }
    }
}