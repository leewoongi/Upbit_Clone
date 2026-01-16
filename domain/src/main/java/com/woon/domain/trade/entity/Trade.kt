package com.woon.domain.trade.entity

/**
 * 체결 데이터
 * @property code 마켓 코드 (예: KRW-BTC, KRW-ETH)
 * @property tradePrice 체결 가격
 * @property tradeVolume 체결량
 * @property askBid 매수/매도 구분 (ASK: 매도, BID: 매수)
 * @property prevClosingPrice 전일 종가
 * @property change 전일 대비 변화 (RISE: 상승, EVEN: 보합, FALL: 하락)
 * @property changePrice 전일 대비 변화 금액
 * @property tradeDate 체결 일자 (UTC 기준, yyyy-MM-dd)
 * @property tradeTime 체결 시각 (UTC 기준, HH:mm:ss)
 * @property tradeTimestamp 체결 타임스탬프 (ms)
 * @property sequentialId 체결 번호 (Unique)
 * @property streamType 스트림 타입 (SNAPSHOT: 스냅샷, REALTIME: 실시간)
 */
data class Trade(
    val code: String,
    val tradePrice: Double,
    val tradeVolume: Double,
    val askBid: String,
    val prevClosingPrice: Double,
    val change: String,
    val changePrice: Double,
    val tradeDate: String,
    val tradeTime: String,
    val tradeTimestamp: Long,
    val sequentialId: Long,
    val streamType: String
)
