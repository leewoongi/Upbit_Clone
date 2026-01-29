# Component

차트의 구성 요소들을 정의합니다.

## 패키지 구조

```
component/
├── PriceScale.kt      # 가격 눈금 (Y축)
├── TimeScale.kt       # 시간 눈금 (X축)
└── indicator/         # 지표 컴포넌트
    ├── grid/          # 그리드
    ├── crosshair/     # 십자선
    └── bollingerband/ # 볼린저 밴드
```

---

## PriceScale

차트 오른쪽에 표시되는 가격 눈금입니다.

### 사용

TradingChart 내부에서 자동으로 렌더링됩니다.

```kotlin
PriceScale(
    state = tradingChartState,
    modifier = Modifier
        .width(60.dp)
        .fillMaxHeight()
)
```

---

## TimeScale

차트 하단에 표시되는 시간 눈금입니다.

### 사용

TradingChart 내부에서 자동으로 렌더링됩니다.

```kotlin
TimeScale(
    state = tradingChartState,
    modifier = Modifier
        .fillMaxWidth()
        .height(24.dp)
)
```

---

## Indicator 패키지

각 지표는 다음 구조를 따릅니다:

```
indicator/
└── {지표명}/
    ├── {지표명}.kt           # DrawScope 확장 함수
    └── model/
        ├── {지표명}Config.kt  # 설정 (Model + Style)
        └── {지표명}Style.kt   # 스타일
```

자세한 내용은 각 지표의 README를 참조하세요.