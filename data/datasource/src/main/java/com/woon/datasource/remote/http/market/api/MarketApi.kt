package com.woon.datasource.remote.http.market.api

import com.woon.datasource.remote.http.market.response.MarketResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MarketApi {

    @GET("market/all")
    suspend fun getMarkets(
        @Query("is_details") isDetails: Boolean = true
    ): List<MarketResponse>
}
