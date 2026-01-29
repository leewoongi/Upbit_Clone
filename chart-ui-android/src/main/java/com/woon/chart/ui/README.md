# UI

메인 차트 컴포넌트와 상태 관리를 담당합니다.

## 파일 구조

```
ui/
├── TradingChart.kt        # 메인 Composable
├── TradingChartState.kt   # 차트 상태 관리
├── IndicatorState.kt      # 지표 상태 관리
├── theme/                 # 테마 시스템
└── extension/             # 유틸리티 확장 함수
```

---

## TradingChart

메인 차트 Composable입니다.

### 파라미터

| 파라미터 | 타입 | 기본값 | 설명 |
|----------|------|--------|------|
| `candles` | `List<TradingCandle>` | 필수 | 캔들 데이터 |
| `modifier` | `Modifier` | `Modifier` | Compose Modifier |
| `state` | `TradingChartState` | `rememberTradingCanvasState()` | 차트 상태 |
| `grid` | `GridConfig.Builder` | `GridConfig.builder()` | 그리드 설정 |
| `crosshair` | `CrosshairConfig.Builder` | `CrosshairConfig.builder()` | 십자선 설정 |
| `bollingerBand` | `BollingerBandConfig.Builder` | `BollingerBandConfig.builder()` | 볼린저 밴드 설정 |
| `strokeWidth` | `Float` | `2f` | 캔들 꼬리 두께 |
| `minBodyHeight` | `Float` | `1f` | 최소 몸통 높이 |
| `risingColor` | `Color` | `chartRisingColor` | 양봉 색상 |
| `fallingColor` | `Color` | `chartFallingColor` | 음봉 색상 |
| `priceScaleWidth` | `Dp` | `60.dp` | 가격 눈금 너비 |
| `timeScaleHeight` | `Dp` | `24.dp` | 시간 눈금 높이 |
| `onReachStart` | `() -> Unit` | `{}` | 스크롤 시작점 도달 콜백 |

### 사용 예시

```kotlin
TradingChart(
    candles = candles,
    modifier = Modifier.fillMaxSize(),
    grid = GridConfig.builder().lineCount(5),
    crosshair = CrosshairConfig.builder().color(Color.White),
    bollingerBand = BollingerBandConfig.builder().enabled(true),
    onReachStart = { viewModel.loadMoreCandles() }
)
```

---

## TradingChartState

차트의 뷰포트 상태를 관리합니다.

### 생성

```kotlin
val state = rememberTradingCanvasState(
    visibleCount = 100,      // 기본 보이는 캔들 수
    candleSpacing = 0.2f,    // 캔들 간격 비율
    minVisibleCount = 24,    // 최소 보이는 캔들 수 (최대 줌)
    maxVisibleCount = 120,   // 최대 보이는 캔들 수 (최소 줌)
    startPadding = 0.1f      // 왼쪽 여백 비율
)
```

### 주요 속성

| 속성 | 타입 | 설명 |
|------|------|------|
| `candles` | `List<TradingCandle>` | 캔들 데이터 |
| `screenWidth` / `screenHeight` | `Float` | 화면 크기 |
| `scale` | `Float` | 현재 줌 배율 |
| `scrollOffset` | `Float` | 스크롤 오프셋 |
| `visibleStartIndex` | `Int` | 보이는 첫 캔들 인덱스 |
| `visibleCandles` | `List<TradingCandle>` | 보이는 캔들 목록 |
| `minPrice` / `maxPrice` | `Double` | 보이는 가격 범위 |
| `isNearStart` | `Boolean` | 시작점 근처 여부 (prefetch용) |

### 좌표 변환 메서드

```kotlin
// 캔들 인덱스 → 화면 X 좌표
fun indexToScreenX(index: Int): Float

// 가격 → 화면 Y 좌표
fun priceToY(price: Double): Float

// 타임스탬프 → 화면 X 좌표
fun timestampToScreenX(timestamp: Long): Float
```

### 제스처 메서드

```kotlin
// 스크롤
fun scroll(deltaX: Float)

// 줌 (focusX 기준)
fun zoomAt(zoomFactor: Float, focusX: Float)
```

---

## IndicatorState

지표들의 상태를 관리합니다.

### 주요 속성

| 속성 | 타입 | 설명 |
|------|------|------|
| `gridConfig` | `GridConfig` | 그리드 설정 |
| `crosshairConfig` | `CrosshairConfig` | 십자선 설정 |
| `bollingerBandConfig` | `BollingerBandConfig` | 볼린저 밴드 설정 |
| `priceRange` | `Pair<Double, Double>?` | 지표 포함 가격 범위 |

### 메서드

```kotlin
// 설정 업데이트
fun update(config: Any)

// 십자선 토글
fun toggleCrosshair(x: Float, y: Float)

// 십자선 이동
fun moveCrosshair(x: Float, y: Float)
```