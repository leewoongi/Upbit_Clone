package com.woon.domain.trade.service

import com.woon.domain.trade.entity.Trade
import com.woon.domain.trade.exception.TradeException
import com.woon.domain.trade.repository.TradeRepository
import com.woon.domain.trade.usecase.ObserveTradesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.SSLException

class ObserveTradesService @Inject constructor(
    private val tradeRepository: TradeRepository
) : ObserveTradesUseCase {

    override suspend fun connect() {
        try {
            tradeRepository.connect()
        } catch (e: TradeException) {
            throw e
        } catch (e: SocketTimeoutException) {
            throw TradeException.TimeoutException(cause = e)
        } catch (e: UnknownHostException) {
            throw TradeException.NetworkException(cause = e)
        } catch (e: SSLException) {
            throw TradeException.SSLException(cause = e)
        } catch (e: java.io.IOException) {
            throw TradeException.WebSocketConnectionException(cause = e)
        } catch (e: Exception) {
            throw TradeException.UnknownException(e.message ?: "알 수 없는 에러가 발생했습니다", e)
        }
    }

    override suspend fun disconnect() {
        try {
            tradeRepository.disconnect()
        } catch (e: Exception) {
            // disconnect 실패는 무시
        }
    }

    override fun invoke(codes: List<String>): Flow<Trade> {
        return tradeRepository.observeTrades(codes)
            .catch { e ->
                throw e.toTradeException()
            }
    }

    private fun Throwable.toTradeException(): TradeException {
        return when (this) {
            is TradeException -> this
            is SocketTimeoutException -> TradeException.TimeoutException(cause = this)
            is UnknownHostException -> TradeException.NetworkException(cause = this)
            is SSLException -> TradeException.SSLException(cause = this)
            is java.io.IOException -> TradeException.WebSocketConnectionException(cause = this)
            else -> TradeException.UnknownException(message ?: "알 수 없는 에러가 발생했습니다", this)
        }
    }
}
