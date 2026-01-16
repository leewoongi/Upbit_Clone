package com.woon.core.ui.design.component.tab

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.woon.core.ui.design.theme.color.colorOnPrimary
import com.woon.core.ui.design.theme.color.colorOnSurfaceVariant
import com.woon.core.ui.design.theme.color.colorOutline
import com.woon.core.ui.design.theme.color.colorPrimary
import com.woon.core.ui.design.theme.color.colorSurface

object ChartTabRowDefaults {
    // Container
    val containerColor: Color
        @Composable get() = colorSurface

    val transparentContainerColor: Color
        @Composable get() = Color.Transparent

    // Content
    val contentColor: Color
        @Composable get() = colorOnSurfaceVariant

    val selectedContentColor: Color
        @Composable get() = colorOnPrimary

    // Background
    val selectedBackgroundColor: Color
        @Composable get() = colorPrimary

    val unselectedBackgroundColor: Color
        @Composable get() = Color.Transparent

    // Divider
    val dividerColor: Color
        @Composable get() = colorOutline

    val dividerThickness: Dp = 1.dp

    // Shape
    val shape: Shape = RoundedCornerShape(4.dp)

    // Border
    val borderWidth: Dp = 1.dp

    val borderColor: Color
        @Composable get() = colorOutline

    val HorizontalTextPadding = 16.dp

    val TabRowIndicatorSpec: AnimationSpec<Dp> =
        tween(durationMillis = 250, easing = FastOutSlowInEasing)
}