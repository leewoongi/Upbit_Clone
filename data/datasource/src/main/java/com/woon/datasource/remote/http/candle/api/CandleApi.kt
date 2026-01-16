package com.woon.datasource.remote.http.candle.api

import com.woon.datasource.remote.http.candle.response.CandleResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CandleApi {
    /**
     * 캔들 조회 API
     * 초 단위
     * @param market 시장명
     * @param to 지정한 시각 이전 캔들 조회/ 미 지정시 현재 시각을 기준
     * @param count 조회 개수 (1 ~ 200)
     * @param unit 분 단위 ( 1, 3, 5, 10, 15, 30, 60, 240 )
     *
     */
    @GET("candles/seconds")
    suspend fun getSecondCandle(
        @Query("market") market: String,
        @Query("to") to: String,
        @Query("count") count: Int
    ): List<CandleResponse>

    /**
     * 캔들 조회 API
     * 분 단위
     * @param market 시장명
     * @param to 지정한 시각 이전 캔들 조회/ 미 지정시 현재 시각을 기준
     * @param count 조회 개수 (1 ~ 200)
     * @param unit 분 단위 ( 1, 3, 5, 10, 15, 30, 60, 240 )
     */
    @GET("candles/minutes/{unit}")
    suspend fun getMinuteCandle(
        @Path("unit") unit: Int,
        @Query("market") market: String,
        @Query("to") to: String,
        @Query("count") count: Int
    ): List<CandleResponse>
}