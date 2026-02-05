package com.woon.core.ui.provides

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController

val LocalNavController = staticCompositionLocalOf<NavController> {
    error("NavController not provided")
}

typealias ClickTracker = (name: String, attrs: Map<String, String>) -> Unit

val LocalClickTracker = staticCompositionLocalOf<ClickTracker> {
    { _, _ -> } // 기본값: no-op
}
