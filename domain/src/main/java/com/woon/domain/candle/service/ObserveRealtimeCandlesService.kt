package com.woon.domain.candle.service

import com.woon.domain.candle.entity.Candle
import com.woon.domain.candle.entity.constant.CandleType
import com.woon.domain.candle.exception.CandleException
import com.woon.domain.candle.repository.CandleRepository
import com.woon.domain.candle.usecase.ObserveRealtimeCandlesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.SSLException

class ObserveRealtimeCandlesService @Inject constructor(
    private val candleRepository: CandleRepository
) : ObserveRealtimeCandlesUseCase {

    override fun invoke(
        marketCode: String,
        candleType: CandleType
    ): Flow<Candle> {
        return candleRepository.observeRealtimeCandles(marketCode, candleType)
            .catch { e ->
                throw e.toCandleException()
            }
    }

    private fun Throwable.toCandleException(): CandleException {
        return when (this) {
            is CandleException -> this
            is SocketTimeoutException -> CandleException.TimeoutException(cause = this)
            is UnknownHostException -> CandleException.NetworkException("인터넷 연결을 확인해주세요", this)
            is SSLException -> CandleException.SSLException(cause = this)
            is java.io.IOException -> CandleException.WebSocketConnectionException(cause = this)
            else -> CandleException.UnknownException(message ?: "알 수 없는 에러가 발생했습니다", this)
        }
    }
}
