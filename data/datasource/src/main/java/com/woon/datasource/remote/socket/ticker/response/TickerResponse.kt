package com.woon.datasource.remote.socket.ticker.response

import com.google.gson.annotations.SerializedName
import com.woon.domain.money.entity.Money
import com.woon.domain.ticker.entity.Ticker
import com.woon.domain.ticker.entity.constant.ChangeType

/**
 * 업비트 WebSocket Ticker 응답 데이터 (현재가)
 *
 * 포맷: DEFAULT
 *
 * @property type 데이터 항목 (ticker)
 * @property code 페어 코드 (예: KRW-BTC)
 * @property streamType 스트림 타입 (SNAPSHOT: 스냅샷, REALTIME: 실시간)
 * @property timestamp 타임스탬프 (ms)
 * @property openingPrice 시가
 * @property highPrice 고가
 * @property lowPrice 저가
 * @property tradePrice 현재가
 * @property prevClosingPrice 전일 종가
 * @property change 전일 종가 대비 가격 변동 방향 (RISE: 상승, EVEN: 보합, FALL: 하락)
 * @property changePrice 전일 대비 가격 변동의 절대값
 * @property signedChangePrice 전일 대비 가격 변동 값 (부호 포함)
 * @property changeRate 전일 대비 등락율의 절대값
 * @property signedChangeRate 전일 대비 등락율 (부호 포함)
 * @property tradeVolume 가장 최근 거래량
 * @property accTradeVolume 누적 거래량 (UTC 0시 기준)
 * @property accTradeVolume24h 24시간 누적 거래량
 * @property accTradePrice 누적 거래대금 (UTC 0시 기준)
 * @property accTradePrice24h 24시간 누적 거래대금
 * @property tradeDate 최근 거래 일자 (UTC, yyyyMMdd)
 * @property tradeTime 최근 거래 시각 (UTC, HHmmss)
 * @property tradeTimestamp 체결 타임스탬프 (ms)
 * @property askBid 매수/매도 구분 (ASK: 매도, BID: 매수)
 * @property accAskVolume 누적 매도량
 * @property accBidVolume 누적 매수량
 * @property highest52WeekPrice 52주 최고가
 * @property highest52WeekDate 52주 최고가 달성일 (yyyy-MM-dd)
 * @property lowest52WeekPrice 52주 최저가
 * @property lowest52WeekDate 52주 최저가 달성일 (yyyy-MM-dd)
 * @property marketState 거래상태 (PREVIEW: 입금지원, ACTIVE: 거래지원가능, DELISTED: 거래지원종료)
 * @property delistingDate 거래지원 종료일 ( 문서는 Date, 실제 값은 { } 빈객체도 내려옴 )
 *
 * @see <a href="https://docs.upbit.com/reference/websocket-ticker">업비트 Ticker API</a>
 */
data class TickerResponse(
    @SerializedName("type") val type: String,
    @SerializedName("code") val code: String,
    @SerializedName("stream_type") val streamType: String,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("opening_price") val openingPrice: Double,
    @SerializedName("high_price") val highPrice: Double,
    @SerializedName("low_price") val lowPrice: Double,
    @SerializedName("trade_price") val tradePrice: Double,
    @SerializedName("prev_closing_price") val prevClosingPrice: Double,
    @SerializedName("change") val change: String,
    @SerializedName("change_price") val changePrice: Double,
    @SerializedName("signed_change_price") val signedChangePrice: Double,
    @SerializedName("change_rate") val changeRate: Double,
    @SerializedName("signed_change_rate") val signedChangeRate: Double,
    @SerializedName("trade_volume") val tradeVolume: Double,
    @SerializedName("acc_trade_volume") val accTradeVolume: Double,
    @SerializedName("acc_trade_volume_24h") val accTradeVolume24h: Double,
    @SerializedName("acc_trade_price") val accTradePrice: Double,
    @SerializedName("acc_trade_price_24h") val accTradePrice24h: Double,
    @SerializedName("trade_date") val tradeDate: String,
    @SerializedName("trade_time") val tradeTime: String,
    @SerializedName("trade_timestamp") val tradeTimestamp: Long,
    @SerializedName("ask_bid") val askBid: String,
    @SerializedName("acc_ask_volume") val accAskVolume: Double,
    @SerializedName("acc_bid_volume") val accBidVolume: Double,
    @SerializedName("highest_52_week_price") val highest52WeekPrice: Double,
    @SerializedName("highest_52_week_date") val highest52WeekDate: String,
    @SerializedName("lowest_52_week_price") val lowest52WeekPrice: Double,
    @SerializedName("lowest_52_week_date") val lowest52WeekDate: String,
    @SerializedName("market_state") val marketState: String,
    @SerializedName("delisting_date") val delistingDate: Any?
) {
    fun toDomain() : Ticker {
        return Ticker(
            code = code,
            openingPrice = Money(openingPrice),
            highPrice = Money(highPrice),
            lowPrice = Money(lowPrice),
            tradePrice = Money(tradePrice),
            prevClosingPrice = Money(prevClosingPrice),
            change = when (change) {
                "RISE" -> ChangeType.RISE
                "FALL" -> ChangeType.FALL
                else -> ChangeType.EVEN
            },
            changePrice = Money(changePrice),
            signedChangePrice = Money(signedChangePrice),
            changeRate = changeRate,
            signedChangeRate = signedChangeRate,
            tradeVolume = tradeVolume,
            accTradeVolume = accTradeVolume,
            accTradeVolume24h = accTradeVolume24h,
            accTradePrice = Money(accTradePrice),
            accTradePrice24h = Money(accTradePrice24h),
            tradeDate = tradeDate,
            tradeTime = tradeTime,
            tradeTimestamp = tradeTimestamp,
            highest52WeekPrice = Money(highest52WeekPrice),
            highest52WeekDate = highest52WeekDate,
            lowest52WeekPrice = Money(lowest52WeekPrice),
            lowest52WeekDate = lowest52WeekDate,
            marketState = marketState,
            streamType = streamType
        )
    }
}