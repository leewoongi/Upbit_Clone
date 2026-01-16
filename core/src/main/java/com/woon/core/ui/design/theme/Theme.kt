package com.woon.core.ui.design.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import com.woon.core.ui.design.theme.color.LocalColors
import com.woon.core.ui.design.theme.color.darkChartColors
import com.woon.core.ui.design.theme.color.lightChartColors
import com.woon.core.ui.design.theme.font.LocalTypography
import com.woon.core.ui.design.theme.theme.ChartTheme

@Composable
fun ChartAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme(
            primary = ChartTheme.colors.primary,
            onPrimary = ChartTheme.colors.onPrimary,
            primaryContainer = ChartTheme.colors.primaryContainer,
            onPrimaryContainer = ChartTheme.colors.onPrimaryContainer,
            inversePrimary = ChartTheme.colors.inversePrimary,
            secondary = ChartTheme.colors.secondary,
            onSecondary = ChartTheme.colors.onSecondary,
            secondaryContainer = ChartTheme.colors.secondaryContainer,
            onSecondaryContainer = ChartTheme.colors.onSecondaryContainer,
            tertiary = ChartTheme.colors.tertiary,
            onTertiary = ChartTheme.colors.onTertiary,
            background = ChartTheme.colors.background,
            onBackground = ChartTheme.colors.onBackground,
            surface = ChartTheme.colors.surface,
            onSurface = ChartTheme.colors.onSurface,
            surfaceVariant = ChartTheme.colors.surfaceVariant,
            onSurfaceVariant = ChartTheme.colors.onSurfaceVariant,
            inverseSurface = ChartTheme.colors.inverseSurface,
            inverseOnSurface = ChartTheme.colors.inverseOnSurface,
            error = ChartTheme.colors.error,
            onError = ChartTheme.colors.onError,
            outline = ChartTheme.colors.outline,
            outlineVariant = ChartTheme.colors.outlineVariant,
            scrim = ChartTheme.colors.scrim
        )
        else -> lightColorScheme(
            primary = ChartTheme.colors.primary,
            onPrimary = ChartTheme.colors.onPrimary,
            primaryContainer = ChartTheme.colors.primaryContainer,
            onPrimaryContainer = ChartTheme.colors.onPrimaryContainer,
            inversePrimary = ChartTheme.colors.inversePrimary,
            secondary = ChartTheme.colors.secondary,
            onSecondary = ChartTheme.colors.onSecondary,
            secondaryContainer = ChartTheme.colors.secondaryContainer,
            onSecondaryContainer = ChartTheme.colors.onSecondaryContainer,
            tertiary = ChartTheme.colors.tertiary,
            onTertiary = ChartTheme.colors.onTertiary,
            background = ChartTheme.colors.background,
            onBackground = ChartTheme.colors.onBackground,
            surface = ChartTheme.colors.surface,
            onSurface = ChartTheme.colors.onSurface,
            surfaceVariant = ChartTheme.colors.surfaceVariant,
            onSurfaceVariant = ChartTheme.colors.onSurfaceVariant,
            inverseSurface = ChartTheme.colors.inverseSurface,
            inverseOnSurface = ChartTheme.colors.inverseOnSurface,
            error = ChartTheme.colors.error,
            onError = ChartTheme.colors.onError,
            outline = ChartTheme.colors.outline,
            outlineVariant = ChartTheme.colors.outlineVariant,
            scrim = ChartTheme.colors.scrim
        )
    }

    val chartColors  = if (darkTheme) darkChartColors() else lightChartColors()
    val chartTypography = Typography(
        displayLarge = ChartTheme.typography.displayLarge,
        displayMedium = ChartTheme.typography.displayMedium,
        displaySmall = ChartTheme.typography.displaySmall,
        headlineLarge = ChartTheme.typography.headlineLarge,
        headlineMedium = ChartTheme.typography.headlineMedium,
        headlineSmall = ChartTheme.typography.headlineSmall,
        titleLarge = ChartTheme.typography.titleLarge,
        titleMedium = ChartTheme.typography.titleMedium,
        titleSmall = ChartTheme.typography.titleSmall,
        bodyLarge = ChartTheme.typography.bodyLarge,
        bodyMedium = ChartTheme.typography.bodyMedium,
        bodySmall = ChartTheme.typography.bodySmall,
        labelLarge = ChartTheme.typography.labelLarge,
        labelMedium = ChartTheme.typography.labelMedium,
        labelSmall = ChartTheme.typography.labelSmall
    )


    CompositionLocalProvider(
        LocalColors provides chartColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = chartTypography,
            content = content
        )
    }
}