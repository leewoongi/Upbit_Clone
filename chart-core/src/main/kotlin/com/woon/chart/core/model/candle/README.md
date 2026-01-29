# Candle

캔들스틱 차트의 캔들 데이터를 정의합니다.

## 파일 구조

```
candle/
├── TradingCandle.kt          # 캔들 데이터 클래스
└── extension/
    └── TradingCandleExt.kt   # 캔들 리스트 확장 함수
```

---

## TradingCandle

캔들 하나의 가격 정보를 담는 데이터 클래스입니다.

### 속성

| 속성 | 타입 | 설명 |
|------|------|------|
| `timestamp` | `Long` | 캔들 기준 시각 (Unix timestamp, ms) |
| `open` | `Double` | 시가 |
| `high` | `Double` | 고가 |
| `low` | `Double` | 저가 |
| `close` | `Double` | 종가 |
| `volume` | `Double` | 거래량 |

### 계산 속성

| 속성 | 타입 | 설명 |
|------|------|------|
| `isRising` | `Boolean` | 양봉 여부 (종가 >= 시가) |
| `bodySize` | `Double` | 캔들 몸통 크기 |
| `totalSize` | `Double` | 캔들 전체 크기 (고가 - 저가) |
| `upperWick` | `Double` | 위 꼬리 크기 |
| `lowerWick` | `Double` | 아래 꼬리 크기 |

### 사용 예시

```kotlin
val candle = TradingCandle(
    timestamp = 1700000000000L,
    open = 100.0,
    high = 110.0,
    low = 95.0,
    close = 105.0,
    volume = 1000.0
)

println(candle.isRising)   // true (종가 > 시가)
println(candle.bodySize)   // 5.0
println(candle.totalSize)  // 15.0
```

---

## TradingCandleExt

캔들 리스트에 대한 확장 함수를 제공합니다.

### toBollingerBand()

캔들 리스트에서 볼린저 밴드를 계산합니다.

```kotlin
fun List<TradingCandle>.toBollingerBand(
    period: Int = 20,
    multiplier: Float = 2f
): List<BollingerBand>
```

**파라미터**
- `period`: 이동평균 기간 (기본값: 20)
- `multiplier`: 표준편차 배수 (기본값: 2)

**사용 예시**
```kotlin
val candles: List<TradingCandle> = ...
val bollingerBands = candles.toBollingerBand(period = 20, multiplier = 2f)
```