# Chart Core Model

트레이딩 차트의 핵심 데이터 모델을 정의하는 패키지입니다.

플랫폼 독립적인 순수 Kotlin으로 작성되어 Android, iOS, Desktop 등 어디서든 사용 가능합니다.

## 패키지 구조

```
model/
├── candle/              # 캔들 데이터
│   ├── TradingCandle.kt
│   └── extension/
│       └── TradingCandleExt.kt
│
└── indicator/           # 차트 지표/보조 요소
    ├── grid/            # 그리드
    ├── crosshair/       # 십자선
    └── bollingerband/   # 볼린저 밴드
```

## 설계 원칙

1. **플랫폼 독립적**: 순수 Kotlin, UI 프레임워크 의존성 없음
2. **데이터만 표현**: 색상, 스타일 등 UI 관심사는 chart-ui-android에서 처리
3. **불변 데이터**: 모든 모델은 data class로 불변성 유지