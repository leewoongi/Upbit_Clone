package com.woon.domain.ticker.service

import com.woon.domain.ticker.entity.Ticker
import com.woon.domain.ticker.exception.TickerException
import com.woon.domain.ticker.repository.TickerRepository
import com.woon.domain.ticker.usecase.GetTickersUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.SSLException

class GetTickersService @Inject constructor(
    private val tickerRepository: TickerRepository,
) : GetTickersUseCase {

    override fun invoke(codes: List<String>): Flow<Ticker> {
        return tickerRepository.observeTickers(codes)
            .catch { e ->
                throw e.toTickerException()
            }
    }

    private fun Throwable.toTickerException(): TickerException {
        return when (this) {
            is TickerException -> this
            is SocketTimeoutException -> TickerException.TimeoutException(cause = this)
            is UnknownHostException -> TickerException.NetworkException(cause = this)
            is SSLException -> TickerException.SSLException(cause = this)
            is java.io.IOException -> TickerException.WebSocketConnectionException(cause = this)
            else -> TickerException.UnknownException(message ?: "알 수 없는 에러가 발생했습니다", this)
        }
    }
}
