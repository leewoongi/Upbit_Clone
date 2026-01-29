package com.woon.domain.market.service

import com.woon.domain.market.entity.Market
import com.woon.domain.market.exception.MarketException
import com.woon.domain.market.repository.MarketRepository
import com.woon.domain.market.usecase.GetMarketsUseCase
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.SSLException

class GetMarketsService @Inject constructor(
    private val marketRepository: MarketRepository
) : GetMarketsUseCase {

    override suspend fun invoke(): List<Market> {
        return try {
            val markets = marketRepository.getMarkets()
            if (markets.isEmpty()) {
                throw MarketException.EmptyDataException()
            }
            markets
        } catch (e: MarketException) {
            throw e
        } catch (e: SocketTimeoutException) {
            throw MarketException.TimeoutException(cause = e)
        } catch (e: UnknownHostException) {
            throw MarketException.NetworkException(cause = e)
        } catch (e: SSLException) {
            throw MarketException.SSLException(cause = e)
        } catch (e: java.io.IOException) {
            throw MarketException.NetworkException(cause = e)
        } catch (e: Exception) {
            throw MarketException.UnknownException(e.message ?: "알 수 없는 에러가 발생했습니다", e)
        }
    }

    override suspend fun getKrwMarkets(): List<Market> {
        return invoke().filter { it.code.startsWith("KRW-") }
    }

    override suspend fun getBtcMarkets(): List<Market> {
        return invoke().filter { it.code.startsWith("BTC-") }
    }

    override suspend fun getUsdtMarkets(): List<Market> {
        return invoke().filter { it.code.startsWith("USDT-") }
    }
}
