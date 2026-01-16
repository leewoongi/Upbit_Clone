package com.woon.domain.ticker.entity

import com.woon.domain.money.entity.Money
import com.woon.domain.ticker.entity.constant.ChangeType

/**
 * 현재가 데이터
 * @property code 마켓 코드 (예: KRW-BTC, KRW-ETH)
 * @property openingPrice 시가
 * @property highPrice 고가
 * @property lowPrice 저가
 * @property tradePrice 현재가
 * @property prevClosingPrice 전일 종가
 * @property change 전일 대비 변화 (RISE: 상승, EVEN: 보합, FALL: 하락)
 * @property changePrice 전일 대비 변화 금액 (절대값)
 * @property signedChangePrice 전일 대비 변화 금액 (부호 포함)
 * @property changeRate 전일 대비 등락율 (절대값)
 * @property signedChangeRate 전일 대비 등락율 (부호 포함)
 * @property tradeVolume 최근 거래량
 * @property accTradeVolume 누적 거래량 (UTC 0시 기준)
 * @property accTradeVolume24h 24시간 누적 거래량
 * @property accTradePrice 누적 거래대금 (UTC 0시 기준)
 * @property accTradePrice24h 24시간 누적 거래대금
 * @property tradeDate 최근 거래 일자 (UTC 기준, yyyyMMdd)
 * @property tradeTime 최근 거래 시각 (UTC 기준, HHmmss)
 * @property tradeTimestamp 체결 타임스탬프 (ms)
 * @property highest52WeekPrice 52주 최고가
 * @property highest52WeekDate 52주 최고가 달성일
 * @property lowest52WeekPrice 52주 최저가
 * @property lowest52WeekDate 52주 최저가 달성일
 * @property marketState 거래 상태 (PREVIEW: 입금지원, ACTIVE: 거래지원가능, DELISTED: 거래지원종료)
 * @property streamType 스트림 타입 (SNAPSHOT: 스냅샷, REALTIME: 실시간)
 */
data class Ticker(
    val code: String,
    val openingPrice: Money,
    val highPrice: Money,
    val lowPrice: Money,
    val tradePrice: Money,
    val prevClosingPrice: Money,
    val change: ChangeType,
    val changePrice: Money,
    val signedChangePrice: Money,
    val changeRate: Double,
    val signedChangeRate: Double,
    val tradeVolume: Double,
    val accTradeVolume: Double,
    val accTradeVolume24h: Double,
    val accTradePrice: Money,
    val accTradePrice24h: Money,
    val tradeDate: String,
    val tradeTime: String,
    val tradeTimestamp: Long,
    val highest52WeekPrice: Money,
    val highest52WeekDate: String,
    val lowest52WeekPrice: Money,
    val lowest52WeekDate: String,
    val marketState: String,
    val streamType: String
)
