# Bollinger Band

볼린저 밴드는 1980년대 존 볼린저(John Bollinger)가 개발한 기술적 분석 지표입니다.
이동평균선을 중심으로 표준편차를 이용해 상단/하단 밴드를 형성하며, 가격의 변동성과 상대적 고점/저점을 파악하는 데 사용됩니다.

## 파일 구조

```
bollingerband/
├── BollingerBand.kt           # 밴드 가격 데이터
├── BollingerBandSettings.kt   # 계산 설정
└── extension/
    └── BollingerBandExt.kt    # 리스트 확장 함수
```

---

## 계산 공식

```
SMA = 종가의 N일 단순 이동평균
σ = 종가의 N일 표준편차

상단 = SMA + (k × σ)
중간 = SMA
하단 = SMA - (k × σ)
```

### 기본 파라미터
- **period (N)**: 20 (이동평균 기간)
- **multiplier (k)**: 2.0 (표준편차 배수)

### 계산 예시

```
최근 20개 캔들의 종가 평균 (SMA) = 100,000원
표준편차 = 5,000원
multiplier = 2.0

상단선 = 100,000 + (2.0 × 5,000) = 110,000원
중심선 = 100,000원
하단선 = 100,000 - (2.0 × 5,000) = 90,000원
```

---

## BollingerBand

볼린저 밴드의 상/중/하단 가격을 담는 데이터 클래스입니다.

### 속성

| 속성 | 타입 | 설명 |
|------|------|------|
| `upper` | `Double` | 상단 밴드 가격 (SMA + k*σ) |
| `middle` | `Double` | 중간 밴드 가격 (SMA) |
| `lower` | `Double` | 하단 밴드 가격 (SMA - k*σ) |

---

## BollingerBandSettings

볼린저 밴드 계산에 필요한 설정입니다.

### 속성

| 속성 | 타입 | 기본값 | 설명 |
|------|------|--------|------|
| `enabled` | `Boolean` | `false` | 볼린저 밴드 표시 여부 |
| `period` | `Int` | `20` | 이동평균 기간 (N) |
| `multiplier` | `Float` | `2f` | 표준편차 배수 (k) |

### 사용 예시

```kotlin
val settings = BollingerBandSettings(
    enabled = true,
    period = 20,
    multiplier = 2f
)
```

---

## BollingerBandExt

볼린저 밴드 리스트에 대한 확장 함수입니다.

### getPriceRange()

보이는 영역의 볼린저 밴드 가격 범위를 계산합니다.

```kotlin
fun List<BollingerBand>.getPriceRange(
    startIndex: Int,
    count: Int
): Pair<Double, Double>?
```

**파라미터**
- `startIndex`: 시작 인덱스
- `count`: 개수

**반환값**
- `Pair<최소값, 최대값>` 또는 `null` (데이터 없음)

---

## 표준편차 계산

```kotlin
fun List<Double>.standardDeviation(): Double {
    if (isEmpty()) return 0.0
    val mean = average()
    val variance = map { (it - mean) * (it - mean) }.average()
    return sqrt(variance)
}
```

---

## 해석

| 상황 | 의미 |
|------|------|
| 가격이 상단선 돌파 | 과매수 상태, 하락 반전 가능성 |
| 가격이 하단선 돌파 | 과매도 상태, 상승 반전 가능성 |
| 밴드 폭이 좁아짐 | 변동성 감소, 큰 움직임 예고 |
| 밴드 폭이 넓어짐 | 변동성 증가 |

---

## 전체 사용 흐름

```kotlin
// 1. 설정 생성
val settings = BollingerBandSettings(enabled = true, period = 20)

// 2. 캔들에서 볼린저 밴드 계산 (TradingCandleExt 사용)
val bands = candles.toBollingerBand(
    period = settings.period,
    multiplier = settings.multiplier
)

// 3. 보이는 영역 가격 범위 계산
val priceRange = bands.getPriceRange(startIndex, visibleCount)

// 4. 렌더링 (chart-ui-android)
// 색상은 TradingChartTheme.colors.bollingerUpper/Middle/Lower 사용
```