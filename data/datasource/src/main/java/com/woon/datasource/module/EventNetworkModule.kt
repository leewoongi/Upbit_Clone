package com.woon.datasource.module

import com.google.gson.Gson
import com.woon.datasource.remote.http.event.api.ErrorEventApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EventNetworkModule {
    private const val EVENT_BASE_URL = "http://192.168.45.192:8080/"

    @Provides
    @Singleton
    @Named("EventRetrofit")
    fun provideEventRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(EVENT_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideErrorEventApi(@Named("EventRetrofit") retrofit: Retrofit): ErrorEventApi {
        return retrofit.create(ErrorEventApi::class.java)
    }
}
