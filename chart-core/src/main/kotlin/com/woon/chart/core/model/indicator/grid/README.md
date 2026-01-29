# Grid

차트의 가격 눈금선(그리드) 설정을 정의합니다.

## 파일 구조

```
grid/
└── Grid.kt
```

---

## Grid

그리드 설정을 담는 데이터 클래스입니다.

### 속성

| 속성 | 타입 | 기본값 | 설명 |
|------|------|--------|------|
| `lineCount` | `Int` | `5` | 수평 그리드 라인 개수 |

### 사용 예시

```kotlin
val grid = Grid(lineCount = 5)
```

### 렌더링

실제 그리드 그리기는 chart-ui-android의 `DrawScope.drawGrid()`에서 처리합니다.
색상은 `TradingChartTheme.colors.grid`에서 가져옵니다.