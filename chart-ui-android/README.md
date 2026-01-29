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
│   ├── IndicatorState.kt
│   ├── theme/             # 테마 시스템
│   └── extension/         # 확장 함수
│
├── component/             # 차트 구성 요소
│   ├── PriceScale.kt      # 가격 눈금
│   ├── TimeScale.kt       # 시간 눈금
│   └── indicator/         # 지표 컴포넌트
│       ├── grid/
│       ├── crosshair/
│       └── bollingerband/
│
└── gesture/               # 제스처 처리
    ├── Scroll.kt
    └── Zoom.kt
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
        .period(20)
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

---

## 아키텍처

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