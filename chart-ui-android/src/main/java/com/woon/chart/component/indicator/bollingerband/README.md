# Bollinger Band

볼린저 밴드 지표를 그립니다.

## 파일 구조

```
bollingerband/
├── BollingerBand.kt              # DrawScope 확장 함수
└── model/
    ├── BollingerBandConfig.kt    # 설정 (Settings + Style + Points)
    └── BollingerBandStyle.kt     # 스타일
```

---

## BollingerBandConfig

볼린저 밴드 설정을 담는 클래스입니다.

### Builder 메서드

**Settings**
| 메서드 | 파라미터 | 설명 |
|--------|----------|------|
| `enabled()` | `Boolean` | 활성화 여부 (기본: false) |
| `period()` | `Int` | 이동평균 기간 (기본: 20) |
| `multiplier()` | `Float` | 표준편차 배수 (기본: 2f) |

**Style**
| 메서드 | 파라미터 | 설명 |
|--------|----------|------|
| `upperColor()` | `Color` | 상단 밴드 색상 |
| `middleColor()` | `Color` | 중간 밴드 색상 |
| `lowerColor()` | `Color` | 하단 밴드 색상 |
| `strokeWidth()` | `Float` | 라인 두께 (기본: 1f) |

**Data**
| 메서드 | 파라미터 | 설명 |
|--------|----------|------|
| `candles()` | `List<TradingCandle>` | 캔들 데이터 (빌드 시 자동 계산) |

### 사용 예시

```kotlin
TradingChart(
    candles = candles,
    bollingerBand = BollingerBandConfig.builder()
        .enabled(true)
        .period(20)
        .multiplier(2f)
        .upperColor(chartBollingerUpperColor)
        .middleColor(chartBollingerMiddleColor)
        .lowerColor(chartBollingerLowerColor)
)
```

---

## BollingerBandStyle

볼린저 밴드 스타일을 담는 데이터 클래스입니다.

| 속성 | 타입 | 설명 |
|------|------|------|
| `upperColor` | `Color` | 상단 밴드 색상 |
| `middleColor` | `Color` | 중간 밴드 색상 |
| `lowerColor` | `Color` | 하단 밴드 색상 |
| `strokeWidth` | `Float` | 라인 두께 |

---

## drawBollingerBand()

Canvas에 볼린저 밴드를 그리는 DrawScope 확장 함수입니다.

```kotlin
fun DrawScope.drawBollingerBand(
    state: TradingChartState,
    config: BollingerBandConfig
)
```

### 동작

1. `config.points`에서 보이는 범위의 포인트 추출
2. 각 포인트를 화면 좌표로 변환
3. `Path`로 상단/중간/하단 밴드 연결
4. `drawPath()`로 렌더링

---

## 계산 흐름

```
1. TradingChart에서 Builder에 candles 전달
   bollingerBand = BollingerBandConfig.builder()
       .enabled(true)
       .candles(candles)  // ← 여기서 전달

2. Builder.build()에서 자동 계산
   val points = candles.toBollingerBand(period, multiplier)

3. Canvas에서 렌더링
   drawBollingerBand(state, config)
```

---

## 가격 범위 통합

볼린저 밴드가 활성화되면 `IndicatorState.priceRange`에 반영되어, 차트의 Y축 범위가 자동으로 조정됩니다.