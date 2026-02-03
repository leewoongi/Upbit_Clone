package com.woon.detail.ui.state

import com.woon.chart.core.model.candle.TradingCandle
import com.woon.core.network.NetworkError

/**
 * Detail 화면 UI 상태
 *
 * 네트워크 에러 시 재시도 정보를 포함한다.
 */
sealed class DetailUiState {

    /**
     * 로딩 중
     */
    data object Loading : DetailUiState()

    /**
     * 성공
     *
     * @property marketCode 마켓 코드
     * @property candles 캔들 데이터
     * @property isRefreshing 새로고침 중 여부 (데이터 있는 상태에서 추가 로드)
     */
    data class Success(
        val marketCode: String,
        val candles: List<TradingCandle> = emptyList(),
        val isRefreshing: Boolean = false
    ) : DetailUiState()

    /**
     * 에러
     *
     * @property message 사용자에게 표시할 메시지
     * @property networkError 네트워크 에러 상세 정보 (있는 경우)
     * @property canRetry 재시도 가능 여부
     * @property retryCount 현재까지 재시도 횟수
     * @property isAutoRetrying 자동 재시도 진행 중 여부
     */
    data class Error(
        val message: String,
        val networkError: NetworkError? = null,
        val canRetry: Boolean = true,
        val retryCount: Int = 0,
        val isAutoRetrying: Boolean = false
    ) : DetailUiState() {

        /**
         * 에러 유형 이름
         */
        val errorTypeName: String
            get() = networkError?.type?.name ?: "UNKNOWN"

        /**
         * 재시도 버튼 텍스트
         */
        val retryButtonText: String
            get() = networkError?.retryButtonText ?: "재시도"
    }

    /**
     * 데이터 없음
     */
    data object Empty : DetailUiState()
}
