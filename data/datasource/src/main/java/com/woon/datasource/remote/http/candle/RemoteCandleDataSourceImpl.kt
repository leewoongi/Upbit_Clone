package com.woon.datasource.remote.http.candle

import com.woon.datasource.remote.http.candle.api.CandleApi
import com.woon.datasource.remote.http.candle.response.CandleResponse
import javax.inject.Inject

class RemoteCandleDataSourceImpl
@Inject constructor(
    private val candleApi: CandleApi
): RemoteCandleDataSource {

    override suspend fun getCandle(
        unit: Int
    ): List<CandleResponse> {
        if(unit == 0) {
            return candleApi.getSecondCandle(
                market = "KRW-BTC",
                to = "",
                count = 200
            )
        } else {
            return candleApi.getMinuteCandle(
                unit = 240,
                market = "KRW-BTC",
                to = "",
                count = 200
            )
        }
    }
}