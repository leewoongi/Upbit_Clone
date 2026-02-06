# Upbit Clone - Real-time Cryptocurrency Trading App

실시간 암호화폐 시세 조회 및 트레이딩 차트 앱

## Preview

| Home Screen | Detail Screen |
|-------------|---------------|
| 실시간 시세 목록 | 캔들 차트 + 보조지표 |

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Architecture**: Clean Architecture + MVI
- **DI**: Hilt
- **Async**: Coroutines + Flow
- **Network**: Retrofit + OkHttp + WebSocket
- **Local DB**: Room
- **Build**: Gradle Kotlin DSL + Convention Plugin

---

## Project Structure

```
chart/
├── app/                          # Application Entry Point
├── build-logic/                  # Gradle Convention Plugins
│
├── domain/                       # Business Logic (Pure Kotlin)
│   ├── candle/                   # 캔들 데이터 관리
│   ├── market/                   # 마켓 정보
│   ├── ticker/                   # 실시간 시세
│   ├── trade/                    # 체결 내역
│   ├── breadcrumb/               # 사용자 행동 추적
│   └── event/                    # 에러 리포팅
│
├── data/
│   ├── datasource/               # API + Local DB
│   │   ├── remote/http/          # REST API
│   │   ├── remote/websocket/     # WebSocket
│   │   └── local/room/           # Room Database
│   └── repository/               # Repository 구현
│
├── network/                      # Network Infrastructure
│   └── interceptor/              # OkHttp Interceptors
│
├── core/                         # Shared UI Components
│   └── ui/
│       ├── design/               # Theme, Typography, Colors
│       ├── provides/             # CompositionLocal
│       └── modifier/             # Custom Modifiers
│
├── presenter/
│   ├── main/                     # MainActivity, Navigation
│   ├── home/                     # Home Screen (시세 목록)
│   └── detail/                   # Detail Screen (차트)
│
├── chart-core/                   # Chart Core (Platform Independent)
│   └── model/
│       ├── candle/               # TradingCandle
│       └── indicator/            # MA, Bollinger, Ichimoku
│
└── chart-ui-android/             # Chart UI (Android Compose)
    ├── component/
    │   ├── indicator/            # 보조지표 렌더링
    │   ├── PriceScale.kt         # 가격축
    │   └── TimeScale.kt          # 시간축
    ├── theme/                    # Chart Theme
    └── ui/
        ├── TradingChart.kt       # Main Chart Composable
        └── TradingChartState.kt  # Chart State Management
```

---

## Architecture

### Clean Architecture Layers

```
┌─────────────────────────────────────────────────────────────┐
│                      Presenter Layer                        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │ HomeScreen  │  │DetailScreen │  │   TradingChart      │ │
│  │ + ViewModel │  │ + ViewModel │  │   (chart-ui)        │ │
│  └─────────────┘  └─────────────┘  └─────────────────────┘ │
└────────────────────────────┬────────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────┐
│                       Domain Layer                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐  │
│  │   UseCase    │  │   Entity     │  │   Repository     │  │
│  │              │  │              │  │   (Interface)    │  │
│  └──────────────┘  └──────────────┘  └──────────────────┘  │
└────────────────────────────┬────────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────┐
│                        Data Layer                           │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐  │
│  │  Repository  │  │  DataSource  │  │     Mapper       │  │
│  │   (Impl)     │  │  (API/DB)    │  │                  │  │
│  └──────────────┘  └──────────────┘  └──────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### Module Dependencies

```
app
 └── presenter:main
      ├── presenter:home
      │    ├── domain
      │    ├── core
      │    └── chart-ui-android
      │         └── chart-core
      └── presenter:detail
           ├── domain
           ├── core
           └── chart-ui-android

data:repository
 ├── domain
 └── data:datasource
      └── domain
```

---

## Features

### 1. Real-time Price (WebSocket)
- Upbit WebSocket API 연동
- Flow 기반 실시간 시세 스트림
- 자동 재연결 및 에러 복구

### 2. Candle Chart
- 1분/5분/15분/1시간/일봉 지원
- 핀치 줌 & 스크롤
- 무한 스크롤 (과거 데이터 로드)

### 3. Technical Indicators
- Moving Average (MA)
- Bollinger Bands
- Ichimoku Cloud
- Crosshair

### 4. Error Reporting System
- 자동 에러 수집 및 서버 전송
- Breadcrumb 기반 사용자 행동 추적
- 오프라인 저장 + 자동 재시도

---

## Chart Module

차트 모듈은 **플랫폼 독립적 코어**와 **Android UI 구현**으로 분리되어 있습니다.

### chart-core (Pure Kotlin)

플랫폼에 의존하지 않는 순수 Kotlin 모듈입니다.

```kotlin
// 캔들 데이터 모델
data class TradingCandle(
    val timestamp: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Double
) {
    val isRising: Boolean get() = close >= open
    val bodySize: Double get() = abs(close - open)
}

// 보조지표 계산 로직
// - MA (이동평균선)
// - Bollinger Bands (볼린저 밴드)
// - Ichimoku Cloud (일목균형표)
```

### chart-ui-android (Compose)

Android Compose 기반 차트 UI 구현입니다.

#### TradingChart Composable

```kotlin
@Composable
fun TradingChart(
    candles: List<TradingCandle>,
    modifier: Modifier = Modifier,
    state: TradingChartState = rememberTradingCanvasState(),

    // 보조지표 설정 (Builder 패턴)
    grid: GridConfig.Builder = GridConfig.builder(),
    crosshair: CrosshairConfig.Builder = CrosshairConfig.builder(),
    bollingerBand: BollingerBandConfig.Builder = BollingerBandConfig.builder(),
    ichimokuCloud: IchimokuConfig.Builder = IchimokuConfig.builder(),
    ma: MAConfig.Builder = MAConfig.builder(),
    volume: VolumeConfig.Builder = VolumeConfig.builder(),

    // 스타일
    risingColor: Color = chartRisingColor,
    fallingColor: Color = chartFallingColor,

    // 콜백
    onReachStart: () -> Unit = {},      // 과거 데이터 로드
    onZoom: (zoomFactor: Float) -> Unit = {},
    onScroll: (deltaX: Float) -> Unit = {},
    onCrosshairToggle: (enabled: Boolean) -> Unit = {}
)
```

#### Usage Example

```kotlin
// 기본 사용
TradingChart(
    candles = candleList,
    modifier = Modifier.fillMaxSize()
)

// 보조지표 활성화
TradingChart(
    candles = candleList,
    ma = MAConfig.builder()
        .enabled(true)
        .periods(listOf(5, 10, 20)),
    bollingerBand = BollingerBandConfig.builder()
        .enabled(true)
        .period(20)
        .standardDeviation(2.0),
    onReachStart = { viewModel.loadMoreCandles() }
)
```

#### TradingChartState

차트 상태 관리 클래스입니다.

```kotlin
class TradingChartState(
    val visibleCount: Int,        // 화면에 보이는 캔들 수
    val candleSpacing: Float,     // 캔들 간격 (0.0 ~ 1.0)
    val minVisibleCount: Int,     // 최소 줌 (최소 캔들 수)
    val maxVisibleCount: Int,     // 최대 줌 (최대 캔들 수)
) {
    // 좌표 변환
    fun indexToScreenX(index: Int): Float
    fun priceToY(price: Double): Float
    fun yToPrice(y: Float): Double

    // 스크롤 & 줌
    fun scroll(deltaX: Float)
    fun zoomAt(zoomFactor: Float, focusX: Float)

    // 보이는 범위
    val visibleCandles: List<TradingCandle>
    val visibleStartIndex: Int
    val visibleEndIndex: Int

    // 가격 범위
    val minPrice: Double
    val maxPrice: Double
}
```

#### Indicator Architecture

보조지표는 **Config + Style + Model** 패턴으로 구성됩니다.

```
indicator/
├── ma/
│   ├── MA.kt                 # 렌더링 로직 (DrawScope 확장)
│   └── model/
│       ├── MAConfig.kt       # 설정 (periods, enabled)
│       └── MAStyle.kt        # 스타일 (colors, strokeWidth)
│
├── bollingerband/
│   ├── BollingerBand.kt
│   └── model/
│       ├── BollingerBandConfig.kt
│       └── BollingerBandStyle.kt
│
├── ichimokucloud/
│   ├── IchimokuCloud.kt
│   └── model/
│       ├── IchimokuConfig.kt
│       └── IchimokuStyle.kt
│
├── crosshair/
│   ├── Crosshair.kt
│   └── model/
│       ├── CrosshairConfig.kt
│       └── CrosshairStyle.kt
│
└── grid/
    ├── Grid.kt
    └── model/
        ├── GridConfig.kt
        └── GridStyle.kt
```

---

## Error Reporting System

에러 발생 시 자동으로 수집하여 서버로 전송합니다.

### Data Flow

```
1. Error 발생
   ↓
2. ErrorReporter.report()
   - 에러 타입 분류
   - Breadcrumbs 수집 (최근 30개 사용자 행동)
   - 디바이스/네트워크 정보 수집
   ↓
3. 서버 전송 시도
   ↓
4. 성공 → 완료
   실패 → Room DB 저장 (최대 10개)
   ↓
5. 30초 주기 자동 재시도
```

### Breadcrumb Types

| Type   | Description        | Example                          |
|--------|--------------------|----------------------------------|
| SCREEN | 화면 진입          | "DetailScreen"                   |
| CLICK  | UI 클릭            | "TimeFrameChange"                |
| NAV    | 네비게이션         | "detail/KRW-BTC"                 |
| HTTP   | API 호출           | "GET /candles"                   |
| SYSTEM | 시스템 이벤트      | "NetworkChange"                  |

### Click Tracking (Compose)

```kotlin
// Modifier 확장 함수로 클릭 자동 추적
Button(
    onClick = { doSomething() },
    modifier = Modifier.trackClick("SubmitButton")
)

// 추가 속성과 함께
Box(
    modifier = Modifier.trackClickable(
        name = "CoinItem",
        attrs = mapOf("symbol" to "BTC")
    ) {
        navController.navigate("detail/KRW-BTC")
    }
)
```

---

## Build Configuration

### Convention Plugins

```
build-logic/
├── ApplicationPlugin.kt    # app 모듈
├── FeaturePlugin.kt        # presenter 모듈
├── CorePlugin.kt           # core 모듈
├── DomainPlugin.kt         # domain 모듈 (Pure Kotlin)
├── DataPlugin.kt           # data 모듈
├── NetworkPlugin.kt        # network 모듈
└── HiltPlugin.kt           # Hilt DI
```

### Module Plugin Usage

```kotlin
// presenter/home/build.gradle.kts
plugins {
    id("chart.feature")  // Android + Compose + Hilt + domain/core 의존성
}

// domain/build.gradle.kts
plugins {
    id("chart.domain")   // Pure Kotlin + Coroutines
}
```

---

## Getting Started

```bash
# Clone
git clone https://github.com/leewoongi/Upbit_Clone.git

# Build
./gradlew assembleDebug

# Run
./gradlew installDebug
```

---

## License

MIT License
