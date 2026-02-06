# Chart UI Android

Android Compose 기반 트레이딩 차트 라이브러리입니다.

## 의존성

- **chart-core**: 플랫폼 독립적인 데이터 모델
- **Jetpack Compose**: UI 프레임워크

## 패키지 구조

```
chart-ui-android/
├── ui/                    # 메인 차트 컴포넌트
│   ├── TradingChart.kt    # 메인 Composable
│   ├── TradingChartState.kt
│   ├── indicator/
│   │   └── IndicatorState.kt
│   ├── theme/             # 테마 시스템
│   └── extension/         # 확장 함수
│
├── component/             # 차트 구성 요소
│   ├── PriceScale.kt      # 가격 눈금
│   ├── TimeScale.kt       # 시간 눈금
│   └── indicator/         # 지표 컴포넌트
│       ├── grid/
│       ├── crosshair/
│       ├── ma/
│       ├── bollingerband/
│       ├── ichimokucloud/
│       └── volume/
│
└── gesture/               # 제스처 처리 (내장)
    ├── Scroll
    └── Zoom
```

---

## 빠른 시작

### 기본 사용법

```kotlin
@Composable
fun ChartScreen(candles: List<TradingCandle>) {
    TradingChart(
        candles = candles,
        modifier = Modifier.fillMaxSize()
    )
}
```

### 지표 설정

```kotlin
TradingChart(
    candles = candles,
    grid = GridConfig.builder()
        .lineCount(5)
        .color(Color.Gray),
    crosshair = CrosshairConfig.builder()
        .color(Color.White)
        .dash(10f, 5f),
    bollingerBand = BollingerBandConfig.builder()
        .enabled(true)
        .period(20),
    ma = MAConfig.builder()
        .enabled(true)
        .periods(listOf(5, 10, 20, 60, 120)),
    ichimokuCloud = IchimokuConfig.builder()
        .enabled(true)
)
```

### 테마 적용

```kotlin
TradingChartThemeProvider(
    colors = darkTradingChartColors()
) {
    TradingChart(candles = candles)
}
```

### 콜백 처리

```kotlin
TradingChart(
    candles = candles,
    onReachStart = {
        // 과거 데이터 로드 (무한 스크롤)
        viewModel.loadMoreCandles()
    },
    onZoom = { zoomFactor ->
        // 줌 이벤트 추적
        analytics.trackZoom(zoomFactor)
    },
    onScroll = { deltaX ->
        // 스크롤 이벤트 추적
    },
    onCrosshairToggle = { enabled ->
        // 크로스헤어 토글
    }
)
```

---

## 아키텍처

### 전체 구조

```
┌─────────────────────────────────────────────────────────────┐
│                      TradingChart                           │
│  ┌─────────────────────────────────────────────────────────┐│
│  │                    Canvas Area                          ││
│  │  ┌─────────────────────────────────────────────────┐   ││
│  │  │  Grid → Bollinger → MA → Ichimoku → Candles →   │   ││
│  │  │                                     Crosshair   │   ││
│  │  └─────────────────────────────────────────────────┘   ││
│  │                                                         ││
│  │  Gesture: detectTransformGestures (Zoom/Pan)           ││
│  │           detectTapGestures (Crosshair Toggle)         ││
│  └─────────────────────────────────────────────────────────┘│
│  ┌───────────────────────┐  ┌────────────────────────────┐ │
│  │      PriceScale       │  │        TimeScale           │ │
│  │  (가격 눈금, 세로축)    │  │    (시간 눈금, 가로축)      │ │
│  └───────────────────────┘  └────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

### Model-Style 분리

각 지표는 **Model**(chart-core)과 **Style**(chart-ui-android)로 분리됩니다:

```
chart-core                    chart-ui-android
┌─────────────────┐          ┌─────────────────┐
│ Crosshair       │          │ CrosshairStyle  │
│ - enabled       │          │ - color         │
│ - x, y          │          │ - strokeWidth   │
└─────────────────┘          │ - dashOn/Off    │
        │                    └─────────────────┘
        │                            │
        └──────────┬─────────────────┘
                   ▼
          ┌─────────────────┐
          │ CrosshairConfig │
          │ - model         │
          │ - style         │
          └─────────────────┘
```

### Builder 패턴

모든 지표는 Builder 패턴으로 설정합니다:

```kotlin
GridConfig.builder()
    .lineCount(5)
    .color(chartGridColor)
    .strokeWidth(1f)
    .build()
```

---

## TradingChartState

차트의 모든 상태를 관리하는 핵심 클래스입니다.

### 주요 속성

```kotlin
class TradingChartState(
    val visibleCount: Int = 100,      // 화면에 보이는 기본 캔들 수
    val candleSpacing: Float = 0.2f,  // 캔들 간격 비율 (0.0 ~ 1.0)
    val minVisibleCount: Int = 24,    // 최대 줌인 시 캔들 수
    val maxVisibleCount: Int = 120,   // 최대 줌아웃 시 캔들 수
    val startPadding: Float = 0.1f    // 좌측 여백 비율
) {
    // === 캔들 데이터 ===
    var candles: List<TradingCandle>
    var screenWidth: Float
    var screenHeight: Float

    // === 뷰포트 상태 ===
    var scrollOffset: Float    // 스크롤 오프셋
    var scale: Float           // 줌 스케일

    // === 계산된 값 (derivedStateOf) ===
    val candleWidth: Float          // 캔들 너비
    val candleSlot: Float           // 캔들 슬롯 (여백 포함)
    val candleBodyWidth: Float      // 캔들 몸통 너비

    val visibleStartIndex: Int      // 보이는 첫 캔들 인덱스
    val visibleEndIndex: Int        // 보이는 마지막 캔들 인덱스
    val visibleCandles: List<TradingCandle>

    val minPrice: Double            // 보이는 범위 최저가
    val maxPrice: Double            // 보이는 범위 최고가
    val priceRange: Double

    val candleIntervalMs: Long      // 캔들 간격 (ms)
    val gridIntervalMs: Long        // 시간 그리드 간격 (ms)

    val isNearStart: Boolean        // 시작 부근 도달 여부 (무한 스크롤용)
}
```

### 좌표 변환

```kotlin
// 캔들 인덱스 → 화면 X 좌표
fun indexToScreenX(index: Int): Float

// 가격 → 화면 Y 좌표
fun priceToY(price: Double): Float

// 화면 Y 좌표 → 가격
fun yToPrice(y: Float): Double

// 타임스탬프 → 화면 X 좌표
fun timestampToScreenX(timestamp: Long): Float
```

### 제스처 처리

```kotlin
// 스크롤 (좌우 이동)
fun scroll(deltaX: Float)

// 줌 (특정 지점 기준)
fun zoomAt(zoomFactor: Float, focusX: Float)
```

---

## 지표 상세

### 1. Grid (그리드)

```kotlin
GridConfig.builder()
    .lineCount(5)              // 수평 라인 수
    .color(Color(0x33FFFFFF))  // 라인 색상
    .strokeWidth(1f)           // 라인 두께
    .build()
```

### 2. Crosshair (십자선)

```kotlin
CrosshairConfig.builder()
    .color(Color.White)
    .strokeWidth(1f)
    .dashOn(10f)     // 대시 길이
    .dashOff(5f)     // 대시 간격
    .build()
```

### 3. Moving Average (이동평균선)

```kotlin
MAConfig.builder()
    .enabled(true)
    .periods(listOf(5, 10, 20, 60, 120))  // MA 기간
    .colors(listOf(                        // 각 기간별 색상
        Color.Yellow,
        Color.Green,
        Color.Blue,
        Color.Magenta,
        Color.Cyan
    ))
    .strokeWidth(1.5f)
    .build()
```

### 4. Bollinger Bands (볼린저 밴드)

```kotlin
BollingerBandConfig.builder()
    .enabled(true)
    .period(20)                    // 기간
    .standardDeviation(2.0)        // 표준편차 배수
    .upperColor(Color.Red)         // 상단 밴드 색상
    .middleColor(Color.White)      // 중간선 색상
    .lowerColor(Color.Blue)        // 하단 밴드 색상
    .fillColor(Color(0x20FFFFFF))  // 채움 색상
    .build()
```

### 5. Ichimoku Cloud (일목균형표)

```kotlin
IchimokuConfig.builder()
    .enabled(true)
    .tenkanPeriod(9)      // 전환선 기간
    .kijunPeriod(26)      // 기준선 기간
    .senkouBPeriod(52)    // 선행스팬B 기간
    .displacement(26)      // 선행 이동
    .tenkanColor(Color.Red)
    .kijunColor(Color.Blue)
    .cloudBullishColor(Color(0x3000FF00))  // 상승 구름
    .cloudBearishColor(Color(0x30FF0000))  // 하락 구름
    .build()
```

---

## 렌더링 순서

Canvas에서 지표는 다음 순서로 그려집니다:

```kotlin
Canvas {
    // 1. 배경 요소 (뒤)
    drawGrid(state, gridConfig)

    // 2. 보조지표
    if (bollingerBandConfig.enabled) {
        drawBollingerBand(state, bollingerBandConfig)
    }
    if (maConfig.enabled) {
        drawMovingAverage(state, maConfig)
    }
    if (ichimokuConfig.enabled) {
        drawIchimokuCloud(state, ichimokuConfig)
    }

    // 3. 캔들 (메인)
    visibleCandles.forEachIndexed { i, candle ->
        // 심지 (wick)
        drawLine(...)
        // 몸통 (body)
        drawRect(...)
    }

    // 4. 오버레이 (앞)
    if (crosshairConfig.enabled) {
        drawCrossHair(crosshairConfig)
    }
}
```

---

## 테마 시스템

### 기본 색상

```kotlin
// 상승/하락 색상
val chartRisingColor = Color(0xFFD24F45)   // 빨강 (상승)
val chartFallingColor = Color(0xFF1261C4)  // 파랑 (하락)

// 배경
val chartBackgroundColor = Color(0xFF0D1117)

// 그리드
val chartGridColor = Color(0x33FFFFFF)
```

### 커스텀 테마 적용

```kotlin
TradingChartThemeProvider(
    colors = TradingChartColors(
        rising = Color.Green,
        falling = Color.Red,
        background = Color.Black,
        grid = Color.DarkGray
    )
) {
    TradingChart(candles = candles)
}
```

---

## 성능 최적화

### 1. derivedStateOf 활용

```kotlin
// 불필요한 재계산 방지
val visibleCandles: List<TradingCandle> by derivedStateOf {
    candles.subList(visibleStartIndex, visibleEndIndex)
}
```

### 2. 뷰포트 기반 렌더링

```kotlin
// 화면에 보이는 캔들만 렌더링
state.visibleCandles.forEachIndexed { i, candle ->
    // 보이는 것만 그림
}
```

### 3. 좌표 캐싱

```kotlin
// 매번 계산하지 않고 캐싱된 값 사용
val x = state.indexToScreenX(index)
val y = state.priceToY(price)
```

---

## 확장 예시

### 새로운 지표 추가

```kotlin
// 1. Config 정의
data class RSIConfig(
    val enabled: Boolean = false,
    val period: Int = 14,
    val color: Color = Color.Yellow
) {
    class Builder { ... }
}

// 2. 렌더링 함수 (DrawScope 확장)
fun DrawScope.drawRSI(
    state: TradingChartState,
    config: RSIConfig
) {
    if (!config.enabled) return

    // RSI 계산 및 렌더링
    val rsiValues = calculateRSI(state.candles, config.period)
    // drawPath(...)
}

// 3. TradingChart에 추가
@Composable
fun TradingChart(
    // ...
    rsi: RSIConfig.Builder = RSIConfig.builder()
) {
    Canvas {
        // ...
        drawRSI(state, rsiConfig)
    }
}
```
