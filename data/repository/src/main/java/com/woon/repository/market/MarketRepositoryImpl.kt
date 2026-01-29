package com.woon.repository.market

import com.woon.datasource.remote.http.market.MarketDataSource
import com.woon.domain.market.entity.Market
import com.woon.domain.market.exception.MarketException
import com.woon.domain.market.repository.MarketRepository
import com.woon.repository.market.mapper.toDomain
import retrofit2.HttpException
import javax.inject.Inject

class MarketRepositoryImpl @Inject constructor(
    private val marketDataSource: MarketDataSource
) : MarketRepository {

    override suspend fun getMarkets(): List<Market> {
        return try {
            marketDataSource.getMarkets()
                .map { it.toDomain() }
        } catch (e: HttpException) {
            throw e.toMarketException()
        }
    }

    private fun HttpException.toMarketException(): MarketException {
        return when (code()) {
            429 -> MarketException.RateLimitExceededException(cause = this)
            in 400..499 -> MarketException.ClientException(code(), cause = this)
            in 500..599 -> MarketException.ServerException(code(), cause = this)
            else -> MarketException.UnknownException("HTTP 에러: ${code()}", this)
        }
    }
}
