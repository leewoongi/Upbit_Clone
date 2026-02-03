package com.woon.network.di

import com.woon.network.interceptor.HttpEventInterceptor
import com.woon.network.interceptor.NetworkErrorInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * 네트워크 타임아웃 설정
 *
 * 각 타임아웃의 역할:
 * - connectTimeout: TCP 연결 수립까지의 시간 (DNS 조회 + 핸드셰이크)
 * - readTimeout: 서버로부터 데이터를 읽기까지 대기 시간
 * - writeTimeout: 서버로 데이터를 쓰기까지 대기 시간
 * - callTimeout: 전체 요청-응답 사이클의 최대 시간 (리다이렉트 포함)
 */
object NetworkTimeouts {
    // REST API용 (Candle, Market 등)
    const val CONNECT_TIMEOUT_SEC = 15L     // 연결 타임아웃
    const val READ_TIMEOUT_SEC = 20L        // 읽기 타임아웃
    const val WRITE_TIMEOUT_SEC = 15L       // 쓰기 타임아웃
    const val CALL_TIMEOUT_SEC = 45L        // 전체 요청 타임아웃

    // WebSocket용 (실시간 데이터)
    const val WS_CONNECT_TIMEOUT_SEC = 10L
    const val WS_READ_TIMEOUT_SEC = 0L      // 무제한 (실시간 스트림)
    const val WS_PING_INTERVAL_SEC = 30L    // Ping 간격
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RestClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WebSocketClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpEventInterceptor(): HttpEventInterceptor {
        return HttpEventInterceptor()
    }

    @Provides
    @Singleton
    fun provideNetworkErrorInterceptor(): NetworkErrorInterceptor {
        return NetworkErrorInterceptor()
    }

    /**
     * REST API용 OkHttpClient
     *
     * 특징:
     * - 적절한 타임아웃 설정 (15/20/15/45초)
     * - 네트워크 에러 인터셉터 (에러 로깅 및 변환)
     * - HTTP 이벤트 기록 (Breadcrumb)
     */
    @Provides
    @Singleton
    @RestClient
    fun provideRestOkHttpClient(
        httpEventInterceptor: HttpEventInterceptor,
        networkErrorInterceptor: NetworkErrorInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            // 타임아웃 설정
            .connectTimeout(NetworkTimeouts.CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(NetworkTimeouts.READ_TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(NetworkTimeouts.WRITE_TIMEOUT_SEC, TimeUnit.SECONDS)
            .callTimeout(NetworkTimeouts.CALL_TIMEOUT_SEC, TimeUnit.SECONDS)
            // 인터셉터 (순서 중요: 에러 인터셉터 먼저)
            .addInterceptor(networkErrorInterceptor)
            .addInterceptor(httpEventInterceptor)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC  // BODY는 큰 응답에서 성능 저하
                }
            )
            // 연결 풀 최적화
            .retryOnConnectionFailure(true)
            .build()
    }

    /**
     * WebSocket용 OkHttpClient
     *
     * 특징:
     * - 빠른 연결 타임아웃 (10초)
     * - 무제한 읽기 타임아웃 (실시간 스트림)
     * - Ping/Pong으로 연결 유지
     */
    @Provides
    @Singleton
    @WebSocketClient
    fun provideWebSocketOkHttpClient(
        httpEventInterceptor: HttpEventInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(NetworkTimeouts.WS_CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(NetworkTimeouts.WS_READ_TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(NetworkTimeouts.WRITE_TIMEOUT_SEC, TimeUnit.SECONDS)
            .pingInterval(NetworkTimeouts.WS_PING_INTERVAL_SEC, TimeUnit.SECONDS)
            .addInterceptor(httpEventInterceptor)
            .retryOnConnectionFailure(true)
            .build()
    }

    /**
     * 기본 OkHttpClient (하위 호환성)
     *
     * RestClient를 기본으로 사용
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        @RestClient restClient: OkHttpClient
    ): OkHttpClient = restClient
}
