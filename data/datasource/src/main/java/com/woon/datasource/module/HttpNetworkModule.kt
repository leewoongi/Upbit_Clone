package com.woon.datasource.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.woon.datasource.remote.http.candle.api.CandleApi
import com.woon.datasource.remote.http.market.api.MarketApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HttpNetworkModule {
    private const val BASE_URL = "https://api.upbit.com/v1/"

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideCandleApi(retrofit: Retrofit): CandleApi {
        return retrofit.create(CandleApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMarketApi(retrofit: Retrofit): MarketApi {
        return retrofit.create(MarketApi::class.java)
    }
}