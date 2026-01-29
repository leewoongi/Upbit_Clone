# Trading Chart Theme

트레이딩 차트의 테마 시스템입니다. 시스템 다크모드 자동 감지 및 커스텀 색상을 지원합니다.

## 파일 구조

```
theme/
├── TradingChartColors.kt       # 색상 클래스 및 프리셋
├── TradingChartTheme.kt        # 테마 객체
├── TradingChartColorSupport.kt # Composable 색상 getter
└── TradingChartThemeProvider.kt # 테마 Provider
```

## 기본 사용법

### 1. 테마 Provider로 감싸기

```kotlin
@Composable
fun MyScreen() {
    TradingChartThemeProvider {
        TradingChart(
            candles = candles,
            startIndex = 0,
            visibleCount = 50
        )
    }
}
```

### 2. 색상 직접 사용하기

Composable 내에서 테마 전달 없이 바로 색상 사용 가능:

```kotlin
@Composable
fun MyComponent() {
    val rising = chartRisingColor
    val falling = chartFallingColor
    val background = chartBackgroundColor
    val grid = chartGridColor
    val text = chartTextColor

    // 볼린저 밴드
    val upper = chartBollingerUpperColor
    val middle = chartBollingerMiddleColor
    val lower = chartBollingerLowerColor
}
```

### 3. TradingChartTheme 객체로 접근

```kotlin
@Composable
fun MyComponent() {
    val colors = TradingChartTheme.colors

    Box(modifier = Modifier.background(colors.background)) {
        // ...
    }
}
```

## 프리셋 테마

### 다크 테마 (기본)
```kotlin
TradingChartThemeProvider {
    // darkTradingChartColors() 자동 적용
}
```

### 라이트 테마
```kotlin
TradingChartThemeProvider(
    colors = lightTradingChartColors()
) {
    TradingChart(...)
}
```

### 업비트 스타일
```kotlin
TradingChartThemeProvider(
    colors = upbitTradingChartColors()
) {
    TradingChart(...)
}
```

### 바이낸스 스타일
```kotlin
TradingChartThemeProvider(
    colors = binanceTradingChartColors()
) {
    TradingChart(...)
}
```

## 커스텀 테마

### 완전 커스텀

```kotlin
val myCustomColors = TradingChartColors(
    rising = Color(0xFF00FF00),        // 초록색 상승
    falling = Color(0xFFFF0000),       // 빨간색 하락
    grid = Color(0xFF333333),
    text = Color(0xFFCCCCCC),
    background = Color(0xFF1A1A1A),
    bollingerUpper = Color(0xFF00BFFF),
    bollingerMiddle = Color(0xFFFFD700),
    bollingerLower = Color(0xFF00BFFF),
)

TradingChartThemeProvider(colors = myCustomColors) {
    TradingChart(...)
}
```

### 기존 테마 일부 수정

```kotlin
val customColors = darkTradingChartColors().copy(
    rising = Color(0xFF00FF00),   // 상승만 초록색으로 변경
    falling = Color(0xFFFF0000)   // 하락만 빨간색으로 변경
)

TradingChartThemeProvider(colors = customColors) {
    TradingChart(...)
}
```

## 시스템 다크모드 연동

기본적으로 시스템 다크모드를 자동 감지합니다:

```kotlin
TradingChartThemeProvider(
    darkTheme = isSystemInDarkTheme(),  // 기본값
    // darkTheme = true 이면 darkTradingChartColors()
    // darkTheme = false 이면 lightTradingChartColors()
) {
    TradingChart(...)
}
```

### 강제 다크/라이트 모드

```kotlin
// 항상 다크 모드
TradingChartThemeProvider(darkTheme = true) {
    TradingChart(...)
}

// 항상 라이트 모드
TradingChartThemeProvider(darkTheme = false) {
    TradingChart(...)
}
```

## 색상 목록

| 색상 | Support 변수 | 설명 |
|------|-------------|------|
| `rising` | `chartRisingColor` | 상승 캔들 색상 |
| `falling` | `chartFallingColor` | 하락 캔들 색상 |
| `grid` | `chartGridColor` | 그리드 라인 색상 |
| `text` | `chartTextColor` | 텍스트 색상 |
| `background` | `chartBackgroundColor` | 배경 색상 |
| `bollingerUpper` | `chartBollingerUpperColor` | 볼린저 상단 밴드 |
| `bollingerMiddle` | `chartBollingerMiddleColor` | 볼린저 중심선 |
| `bollingerLower` | `chartBollingerLowerColor` | 볼린저 하단 밴드 |