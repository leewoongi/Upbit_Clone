package com.woon.datasource.remote.http.market.response

import com.google.gson.annotations.SerializedName

/**
 * 마켓(거래쌍) 응답
 * @property market 페어(거래쌍)의 코드 (예: KRW-BTC)
 * @property koreanName 해당 디지털 자산의 한글명
 * @property englishName 해당 디지털 자산의 영문명
 * @property marketEvent 종목 경보 정보
 */
data class MarketResponse(
    @SerializedName("market") val market: String,
    @SerializedName("korean_name") val koreanName: String,
    @SerializedName("english_name") val englishName: String,
    @SerializedName("market_event") val marketEvent: MarketEventResponse?
)

/**
 * 종목 경보 정보
 * @property warning 유의 종목 여부. 업비트의 시장경보 시스템에 따라 해당 페어가 유의 종목으로 지정되었는지 여부
 * @property caution 주의 종목 여부. 주의 종목으로 지정된 경우 세부 경보 유형 중 하나 이상에 해당
 */
data class MarketEventResponse(
    @SerializedName("warning") val warning: Boolean,
    @SerializedName("caution") val caution: MarketCautionResponse?
)

/**
 * 주의 종목 세부 경보 유형
 * @property priceFluctuations 가격 급등락 경보
 * @property tradingVolumeSoaring 거래량 급증 경보
 * @property depositAmountSoaring 입금량 급증 경보
 * @property globalPriceDifferences 국내외 가격 차이 경보
 * @property concentrationOfSmallAccounts 소수 계정 집중 거래 경보
 */
data class MarketCautionResponse(
    @SerializedName("PRICE_FLUCTUATIONS") val priceFluctuations: Boolean,
    @SerializedName("TRADING_VOLUME_SOARING") val tradingVolumeSoaring: Boolean,
    @SerializedName("DEPOSIT_AMOUNT_SOARING") val depositAmountSoaring: Boolean,
    @SerializedName("GLOBAL_PRICE_DIFFERENCES") val globalPriceDifferences: Boolean,
    @SerializedName("CONCENTRATION_OF_SMALL_ACCOUNTS") val concentrationOfSmallAccounts: Boolean
)
