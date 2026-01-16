package com.woon.core.ui.design.component.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun ChartText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    typography: TextStyle,
    textAlign: TextAlign = TextAlign.Start,
    maxLine: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        style = typography,
        textAlign = textAlign,
        maxLines = maxLine,
        overflow = overflow
    )
}