package com.woon.core.ui.design.component.tab

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFold
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.util.fastMap
import com.woon.core.ui.design.component.tab.ChartTabRowDefaults.HorizontalTextPadding

@Composable
fun ChartTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    containerColor: Color = ChartTabRowDefaults.containerColor,
    contentColor: Color = ChartTabRowDefaults.contentColor,
    indicator: @Composable (tabPositions: List<TabPosition>) -> Unit =
        @Composable { tabPositions ->
            if (selectedTabIndex < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                )
            }
        },
    dividerOrientation: TabDividerOrientation,
    divider: @Composable () -> Unit = @Composable {  },
    tabs: @Composable () -> Unit
){
    ChartTabRowWithSubComposeImpl(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        indicator = indicator,
        dividerOrientation = dividerOrientation,
        divider = divider,
        tabs = tabs
    )
}


@Composable
fun ChartTabRowWithSubComposeImpl(
    modifier: Modifier,
    containerColor: Color,
    contentColor: Color,
    indicator: @Composable (tabPositions: List<TabPosition>) -> Unit,
    dividerOrientation: TabDividerOrientation,
    divider: @Composable () -> Unit,
    tabs: @Composable () -> Unit
){
    Surface(
        modifier = modifier.selectableGroup(),
        color = containerColor,
        contentColor = contentColor
    ) {
        SubcomposeLayout(

        ) { constraints ->
            val tabRowWidth = constraints.maxWidth
            val tabMeasurables = subcompose(ChartTabSlots.Tabs, tabs)
            val tabCount = tabMeasurables.size


            // Divider 개수 계산
            val dividerCount = when (dividerOrientation) {
                TabDividerOrientation.VERTICAL -> maxOf(0, tabCount - 1)
                TabDividerOrientation.HORIZONTAL -> 1
            }

            // Divider 측정 (너비 계산용)
            val dividerMeasurables = subcompose(ChartTabSlots.Divider) {
                repeat(dividerCount) { divider() }
            }

            val dividerWidthPx = when (dividerOrientation) {
                TabDividerOrientation.VERTICAL -> {
                    dividerMeasurables.firstOrNull()?.maxIntrinsicWidth(0) ?: 0
                }
                else -> 0
            }
            val totalDividerWidth = dividerWidthPx * dividerCount

            var tabWidth = 0
            if (tabCount > 0) {
                tabWidth = (tabRowWidth / tabCount)
            }
            val tabRowHeight =
                tabMeasurables.fastFold(initial = 0) { max, curr ->
                    maxOf(curr.maxIntrinsicHeight(tabWidth), max)
                }

            val tabPlaceables =
                tabMeasurables.fastMap {
                    it.measure(
                        constraints.copy(
                            minWidth = tabWidth,
                            maxWidth = tabWidth,
                            minHeight = tabRowHeight,
                            maxHeight = tabRowHeight,
                        )
                    )
                }

            // Divider 측정
            val dividerPlaceables = when (dividerOrientation) {
                TabDividerOrientation.VERTICAL -> {
                    dividerMeasurables.map {
                        it.measure(
                            constraints.copy(
                                minHeight = tabRowHeight,
                                maxHeight = tabRowHeight
                            )
                        )
                    }
                }
                TabDividerOrientation.HORIZONTAL -> {
                    dividerMeasurables.map {
                        it.measure(constraints.copy(minHeight = 0))
                    }
                }
            }


            val tabPositions =
                List(tabCount) { index ->
                    var contentWidth =
                        minOf(tabMeasurables[index].maxIntrinsicWidth(tabRowHeight), tabWidth)
                            .toDp()
                    contentWidth -= HorizontalTextPadding * 2
                    // Enforce minimum touch target of 24.dp
                    val indicatorWidth = maxOf(contentWidth, 24.dp)
                    TabPosition(
                        left = tabWidth.toDp() * index,
                        width = tabWidth.toDp(),
                        contentWidth = indicatorWidth
                    )
                }

            layout(tabRowWidth, tabRowHeight) {

                when (dividerOrientation) {
                    TabDividerOrientation.VERTICAL -> {
                        var xOffset = 0
                        tabPlaceables.fastForEachIndexed { index, placeable ->
                            placeable.placeRelative(xOffset, 0)
                            xOffset += placeable.width

                            if (index < dividerPlaceables.size) {
                                dividerPlaceables[index].placeRelative(xOffset, 0)
                                xOffset += dividerWidthPx
                            }
                        }
                    }

                    TabDividerOrientation.HORIZONTAL -> {
                        tabPlaceables.fastForEachIndexed { index, placeable ->
                            placeable.placeRelative(index * tabWidth, 0)
                        }
                        dividerPlaceables.fastForEach { placeable ->
                            placeable.placeRelative(0, tabRowHeight - placeable.height)
                        }
                    }
                }

                subcompose(ChartTabSlots.Indicator) { indicator(tabPositions) }
                    .fastForEach {
                        it.measure(Constraints.fixed(tabRowWidth, tabRowHeight)).placeRelative(0, 0)
                    }
            }
        }
    }
}