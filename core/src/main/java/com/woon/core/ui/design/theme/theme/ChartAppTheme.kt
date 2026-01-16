package com.woon.core.ui.design.theme.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.woon.core.ui.design.theme.color.ChartColors
import com.woon.core.ui.design.theme.color.LocalColors
import com.woon.core.ui.design.theme.font.ChartTypography
import com.woon.core.ui.design.theme.font.LocalTypography

object ChartTheme {
    val colors: ChartColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: ChartTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}
