# Indicator

차트에 표시되는 지표 컴포넌트들입니다.

## 패키지 구조

```
indicator/
├── IndicatorBuilder.kt    # 빌더 인터페이스
├── grid/                  # 그리드
├── crosshair/             # 십자선
└── bollingerband/         # 볼린저 밴드
```

---

## 아키텍처

### Config = Model + Style

각 지표는 **Config** 클래스로 Model과 Style을 묶습니다:

```kotlin
data class CrosshairConfig(
    val model: Crosshair,      // chart-core (데이터)
    val style: CrosshairStyle  // chart-ui-android (UI)
)
```

### Builder 패턴

모든 지표는 Builder 패턴으로 생성합니다:

```kotlin
CrosshairConfig.builder()
    .color(Color.White)        // style 설정
    .strokeWidth(1f)
    .dash(10f, 5f)
    .build()
```

### DrawScope 확장 함수

각 지표는 `DrawScope` 확장 함수로 렌더링합니다:

```kotlin
fun DrawScope.drawCrossHair(config: CrosshairConfig)
fun DrawScope.drawGrid(state: TradingChartState, config: GridConfig)
fun DrawScope.drawBollingerBand(state: TradingChartState, config: BollingerBandConfig)
```

---

## IndicatorBuilder

모든 지표 Builder가 구현하는 인터페이스입니다.

```kotlin
interface IndicatorBuilder {
    fun build(): Any
}
```

---

## 지표 목록

| 지표 | 설명 |
|------|------|
| Grid | 가격/시간 눈금선 |
| Crosshair | 터치 위치 십자선 |
| BollingerBand | 볼린저 밴드 지표 |

각 지표의 상세 내용은 해당 패키지의 README를 참조하세요.