# Grid

차트의 가격/시간 눈금선을 그립니다.

## 파일 구조

```
grid/
├── Grid.kt              # DrawScope 확장 함수
└── model/
    ├── GridConfig.kt    # 설정 (Model + Style)
    └── GridStyle.kt     # 스타일
```

---

## GridConfig

그리드 설정을 담는 클래스입니다.

### Builder 메서드

| 메서드 | 파라미터 | 설명 |
|--------|----------|------|
| `lineCount()` | `Int` | 수평 그리드 라인 수 (기본: 5) |
| `color()` | `Color` | 라인 색상 (기본: `0xFF2A2A2A`) |
| `strokeWidth()` | `Float` | 라인 두께 (기본: 1f) |

### 사용 예시

```kotlin
TradingChart(
    candles = candles,
    grid = GridConfig.builder()
        .lineCount(5)
        .color(chartGridColor)
        .strokeWidth(1f)
)
```

---

## GridStyle

그리드 스타일을 담는 데이터 클래스입니다.

| 속성 | 타입 | 설명 |
|------|------|------|
| `lineColor` | `Color` | 라인 색상 |
| `strokeWidth` | `Float` | 라인 두께 |

---

## drawGrid()

Canvas에 그리드를 그리는 DrawScope 확장 함수입니다.

```kotlin
fun DrawScope.drawGrid(
    state: TradingChartState,
    config: GridConfig
)
```

### 동작

1. **수평선**: `lineCount + 1`개의 가격 눈금선
2. **수직선**: 시간 간격에 맞춰 자동 계산된 시간 눈금선

### 시간 눈금 간격

`TradingChartState.gridIntervalMs`에서 자동 계산됩니다:
- 보이는 시간 범위를 5등분
- 캔들 간격의 배수로 정규화