package com.woon.domain.candle.service

import com.woon.domain.candle.entity.Candle
import com.woon.domain.candle.entity.constant.CandleType
import com.woon.domain.candle.exception.CandleException
import com.woon.domain.candle.repository.CandleRepository
import com.woon.domain.candle.usecase.GetHistoricalCandlesUseCase
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.SSLException

class GetHistoricalCandlesService @Inject constructor(
    private val candleRepository: CandleRepository
) : GetHistoricalCandlesUseCase {

    override suspend fun invoke(
        marketCode: String,
        candleType: CandleType,
        count: Int,
        to: String?
    ): List<Candle> {
        return try {
            val candles = candleRepository.getHistoricalCandles(marketCode, candleType, count, to)
            if (candles.isEmpty()) {
                throw CandleException.EmptyDataException()
            }
            candles
        } catch (e: CandleException) {
            throw e
        } catch (e: SocketTimeoutException) {
            throw CandleException.TimeoutException(cause = e)
        } catch (e: UnknownHostException) {
            throw CandleException.NetworkException(cause = e)
        } catch (e: SSLException) {
            throw CandleException.SSLException(cause = e)
        } catch (e: java.io.IOException) {
            throw CandleException.NetworkException(cause = e)
        } catch (e: Exception) {
            throw CandleException.UnknownException(e.message ?: "알 수 없는 에러가 발생했습니다", e)
        }
    }
}
