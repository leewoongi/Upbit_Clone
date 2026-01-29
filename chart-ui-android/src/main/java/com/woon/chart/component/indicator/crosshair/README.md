# Crosshair

터치 위치를 표시하는 십자선입니다.

## 파일 구조

```
crosshair/
├── Crosshair.kt              # DrawScope 확장 함수
└── model/
    ├── CrosshairConfig.kt    # 설정 (Model + Style)
    └── CrosshairStyle.kt     # 스타일
```

---

## CrosshairConfig

십자선 설정을 담는 클래스입니다.

### Builder 메서드

| 메서드 | 파라미터 | 설명 |
|--------|----------|------|
| `color()` | `Color` | 라인 색상 (기본: `0xFF000000`) |
| `strokeWidth()` | `Float` | 라인 두께 (기본: 1f) |
| `dash()` | `on: Float, off: Float` | 대시 패턴 (기본: 10f, 10f) |

### 상태 변경 메서드

| 메서드 | 설명 |
|--------|------|
| `show(x, y)` | 십자선 표시 |
| `moveTo(x, y)` | 십자선 이동 |
| `hide()` | 십자선 숨김 |

### 사용 예시

```kotlin
TradingChart(
    candles = candles,
    crosshair = CrosshairConfig.builder()
        .color(Color.White)
        .strokeWidth(1f)
        .dash(10f, 5f)
)
```

---

## CrosshairStyle

십자선 스타일을 담는 데이터 클래스입니다.

| 속성 | 타입 | 설명 |
|------|------|------|
| `color` | `Color` | 라인 색상 |
| `strokeWidth` | `Float` | 라인 두께 |
| `dashOn` | `Float` | 대시 길이 |
| `dashOff` | `Float` | 대시 간격 |

---

## drawCrossHair()

Canvas에 십자선을 그리는 DrawScope 확장 함수입니다.

```kotlin
fun DrawScope.drawCrossHair(config: CrosshairConfig)
```

### 동작

1. `model.enabled`가 `true`일 때만 렌더링
2. 수직선: `(model.x, 0)` → `(model.x, height)`
3. 수평선: `(0, model.y)` → `(width, model.y)`
4. `PathEffect.dashPathEffect`로 대시 패턴 적용

---

## 제스처 연동

TradingChart에서 자동으로 제스처와 연동됩니다:

- **탭**: 십자선 토글 (표시/숨김)
- **드래그** (십자선 활성 시): 십자선 이동
- **드래그** (십자선 비활성 시): 차트 스크롤