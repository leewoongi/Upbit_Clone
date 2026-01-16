package com.woon.core.ui.design.component.tab

import androidx.compose.material3.TabPosition
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

@Immutable
class TabPosition internal constructor(
    val left: Dp, val width: Dp, val contentWidth: Dp
) {

    val right: Dp
        get() = left + width

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TabPosition) return false

        if (left != other.left) return false
        if (width != other.width) return false
        if (contentWidth != other.contentWidth) return false

        return true
    }

    override fun hashCode(): Int {
        var result = left.hashCode()
        result = 31 * result + width.hashCode()
        result = 31 * result + contentWidth.hashCode()
        return result
    }

    override fun toString(): String {
        return "TabPosition(left=$left, right=$right, width=$width, contentWidth=$contentWidth)"
    }
}
