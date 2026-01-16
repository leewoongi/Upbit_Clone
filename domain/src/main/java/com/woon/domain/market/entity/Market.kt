package com.woon.domain.market.entity

/**
 * 마켓(거래쌍) 정보
 * @property code 마켓 코드 (예: KRW-BTC)
 * @property koreanName 한글명 (예: 비트코인)
 * @property englishName 영문명 (예: Bitcoin)
 * @property marketWarning 유의 종목 여부
 * @property caution 주의 종목 정보
 */
data class Market(
    val code: String,
    val koreanName: String,
    val englishName: String,
    val marketWarning: Boolean,
    val caution: MarketCaution?
)

/**
 * 주의 종목 정보
 * @property priceFluctuations 가격 급등락 경보
 * @property tradingVolumeSoaring 거래량 급등 경보
 * @property depositAmountSoaring 입금량 급등 경보
 * @property globalPriceDifferences 가격 차이 경보
 * @property concentrationOfSmallAccounts 소액 계정 집중 경보
 */
data class MarketCaution(
    val priceFluctuations: Boolean,
    val tradingVolumeSoaring: Boolean,
    val depositAmountSoaring: Boolean,
    val globalPriceDifferences: Boolean,
    val concentrationOfSmallAccounts: Boolean
)
