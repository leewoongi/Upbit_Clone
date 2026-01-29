# Crosshair

사용자 터치 위치를 표시하는 십자선 데이터를 정의합니다.

## 파일 구조

```
crosshair/
└── Crosshair.kt
```

---

## Crosshair

십자선의 상태와 위치를 담는 데이터 클래스입니다.

### 속성

| 속성 | 타입 | 기본값 | 설명 |
|------|------|--------|------|
| `enabled` | `Boolean` | `false` | 십자선 표시 여부 |
| `x` | `Float` | `0f` | 십자선 X 좌표 (화면 픽셀) |
| `y` | `Float` | `0f` | 십자선 Y 좌표 (화면 픽셀) |

### 사용 예시

```kotlin
// 초기 상태 (비활성화)
var crosshair = Crosshair()

// 터치 시작 - 십자선 표시
crosshair = crosshair.copy(enabled = true, x = touchX, y = touchY)

// 터치 이동
crosshair = crosshair.copy(x = newX, y = newY)

// 터치 종료 - 십자선 숨김
crosshair = crosshair.copy(enabled = false)
```

### 렌더링

실제 십자선 그리기는 chart-ui-android에서 처리합니다.
색상, 선 두께, 대시 패턴 등 스타일은 렌더링 시점에 적용됩니다.

```kotlin
// chart-ui-android에서의 사용 예시
fun DrawScope.drawCrosshair(
    crosshair: Crosshair,
    color: Color,           // 테마에서 가져옴
    strokeWidth: Float,
    dashPattern: FloatArray
) {
    if (!crosshair.enabled) return
    // 그리기 로직...
}
```