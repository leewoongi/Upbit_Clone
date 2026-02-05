package com.woon.core.ui.modifier

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role
import com.woon.core.ui.provides.LocalClickTracker

/**
 * 클릭 이벤트를 추적하는 Modifier
 *
 * 사용 예시:
 * ```
 * Button(
 *     onClick = { /* 실제 동작 */ },
 *     modifier = Modifier.trackClick("SettingsButton")
 * )
 *
 * // 추가 정보와 함께
 * IconButton(
 *     onClick = { /* 동작 */ },
 *     modifier = Modifier.trackClick(
 *         name = "FavoriteButton",
 *         attrs = mapOf("coinId" to coin.id, "screen" to "Home")
 *     )
 * )
 * ```
 */
fun Modifier.trackClick(
    name: String,
    attrs: Map<String, String> = emptyMap()
): Modifier = composed {
    val clickTracker = LocalClickTracker.current

    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = {
            clickTracker(name, attrs)
        }
    )
}

/**
 * 클릭 이벤트를 추적하면서 실제 동작도 수행하는 Modifier
 *
 * 사용 예시:
 * ```
 * Box(
 *     modifier = Modifier.trackClickable(
 *         name = "CoinItem",
 *         attrs = mapOf("symbol" to coin.symbol)
 *     ) {
 *         navController.navigate("detail/${coin.id}")
 *     }
 * )
 * ```
 */
fun Modifier.trackClickable(
    name: String,
    attrs: Map<String, String> = emptyMap(),
    enabled: Boolean = true,
    role: Role? = null,
    onClick: () -> Unit
): Modifier = composed {
    val clickTracker = LocalClickTracker.current

    this.clickable(
        enabled = enabled,
        role = role,
        onClick = {
            clickTracker(name, attrs)
            onClick()
        }
    )
}

/**
 * 클릭 이벤트만 추적 (기존 clickable과 함께 사용)
 *
 * 사용 예시:
 * ```
 * Button(
 *     onClick = { doSomething() },
 *     modifier = Modifier.onClickTrack("SubmitButton", mapOf("formId" to formId))
 * )
 * ```
 *
 * 주의: 이 Modifier는 클릭 시 추적만 하고 클릭 동작은 수행하지 않습니다.
 * 별도의 onClick 핸들러와 함께 사용하세요.
 */
@Composable
fun Modifier.onClickTrack(
    name: String,
    attrs: Map<String, String> = emptyMap()
): Modifier {
    val clickTracker = LocalClickTracker.current

    return this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = { clickTracker(name, attrs) }
    )
}
