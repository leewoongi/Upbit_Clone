package com.woon.datasource.remote.http.candle.api

import com.woon.datasource.remote.http.candle.response.CandleResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 캔들 조회 API
 *
 * Rate Limit: 초당 10회 (캔들 그룹 공유)
 */
interface CandleApi {

    /**
     * 초봉 조회
     *
     * @param market 마켓 코드 (ex. "KRW-BTC")
     * @param count 조회 개수 (최대 200)
     * @param to 조회 종료 시각 (ISO 8601)
     * @return 초봉 리스트
     */
    @GET("candles/seconds")
    suspend fun getSecondCandles(
        @Query("market") market: String,
        @Query("count") count: Int = 200,
        @Query("to") to: String? = null
    ): List<CandleResponse>

    /**
     * 분봉 조회
     *
     * @param unit 분 단위 (1, 3, 5, 10, 15, 30, 60, 240)
     * @param market 마켓 코드 (ex. "KRW-BTC")
     * @param count 조회 개수 (최대 200)
     * @param to 조회 종료 시각 (ISO 8601)
     * @return 분봉 리스트
     */
    @GET("candles/minutes/{unit}")
    suspend fun getMinuteCandles(
        @Path("unit") unit: Int,
        @Query("market") market: String,
        @Query("count") count: Int = 200,
        @Query("to") to: String? = null
    ): List<CandleResponse>

    /**
     * 일봉 조회
     *
     * @param market 마켓 코드 (ex. "KRW-BTC")
     * @param count 조회 개수 (최대 200)
     * @param to 조회 종료 시각 (ISO 8601)
     * @param convertingPriceUnit 종가 환산 통화 (ex. "KRW")
     * @return 일봉 리스트
     */
    @GET("candles/days")
    suspend fun getDayCandles(
        @Query("market") market: String,
        @Query("count") count: Int = 200,
        @Query("to") to: String? = null,
        @Query("convertingPriceUnit") convertingPriceUnit: String? = null
    ): List<CandleResponse>

    /**
     * 주봉 조회
     *
     * @param market 마켓 코드 (ex. "KRW-BTC")
     * @param count 조회 개수 (최대 200)
     * @param to 조회 종료 시각 (ISO 8601)
     * @return 주봉 리스트
     */
    @GET("candles/weeks")
    suspend fun getWeekCandles(
        @Query("market") market: String,
        @Query("count") count: Int = 200,
        @Query("to") to: String? = null
    ): List<CandleResponse>

    /**
     * 월봉 조회
     *
     * @param market 마켓 코드 (ex. "KRW-BTC")
     * @param count 조회 개수 (최대 200)
     * @param to 조회 종료 시각 (ISO 8601)
     * @return 월봉 리스트
     */
    @GET("candles/months")
    suspend fun getMonthCandles(
        @Query("market") market: String,
        @Query("count") count: Int = 200,
        @Query("to") to: String? = null
    ): List<CandleResponse>
}