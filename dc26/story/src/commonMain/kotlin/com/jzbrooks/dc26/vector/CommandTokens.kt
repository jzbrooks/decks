package com.jzbrooks.dc26.vector

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import com.jzbrooks.dc26.theme.VgoColors

@Composable
fun List<PathCommand>.commandTokens(highlightIndex: Int = -1): List<AnnotatedString> =
    toTokens(
        letterStyle = SpanStyle(color = MaterialTheme.colors.secondary),
        numberStyle = SpanStyle(color = VgoColors.OnDark),
        highlightIndex = highlightIndex,
        highlightStyle = SpanStyle(background = VgoColors.Violet.copy(alpha = 0.45f)),
    )
