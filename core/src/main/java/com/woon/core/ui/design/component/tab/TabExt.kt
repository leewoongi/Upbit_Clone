package com.woon.core.ui.design.component.tab

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.IntOffset
import com.woon.core.ui.design.component.tab.ChartTabRowDefaults.TabRowIndicatorSpec

internal fun Modifier.tabIndicatorOffset(
    currentTabPosition: TabPosition
): Modifier =
    composed(
        inspectorInfo =
            debugInspectorInfo {
                name = "tabIndicatorOffset"
                value = currentTabPosition
            }
    ) {
        val currentTabWidth by
        animateDpAsState(
            targetValue = currentTabPosition.width,
            animationSpec = TabRowIndicatorSpec
        )
        val indicatorOffset by
        animateDpAsState(
            targetValue = currentTabPosition.left,
            animationSpec = TabRowIndicatorSpec
        )
        fillMaxWidth()
            .wrapContentSize(Alignment.BottomStart)
            .offset { IntOffset(x = indicatorOffset.roundToPx(), y = 0) }
            .width(currentTabWidth)
    }