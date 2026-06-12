package com.jzbrooks.dc26.vector

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.jzbrooks.dc26.template.ByteChip
import com.jzbrooks.dc26.template.CodeTextStyle
import com.jzbrooks.dc26.theme.VgoColors
import dev.bnorm.storyboard.text.magic.MagicText

@Composable
fun PathPanel(
    label: String,
    commands: List<PathCommand>,
    viewport: Rect,
    color: Color,
    modifier: Modifier = Modifier,
    canvasSize: Dp = 230.dp,
    showByteCount: Boolean = true,
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        PathCanvas(
            commands = commands,
            viewport = viewport,
            stroke = color,
            fill = color.copy(alpha = 0.18f),
            modifier = Modifier
                .size(canvasSize)
                .clip(RoundedCornerShape(16.dp))
                .background(VgoColors.Surface),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(label, color = color, style = MaterialTheme.typography.body1)
            if (showByteCount) ByteChip(commands.byteCount(), color = color)
        }
    }
}

/**
 * The standard optimization-pass demo: before (amber) and after (azure)
 * side by side, with the path string morphing between spellings below.
 */
@Composable
fun BeforeAfterPaths(
    before: List<PathCommand>,
    after: List<PathCommand>,
    viewport: Rect,
    showAfter: Boolean,
    modifier: Modifier = Modifier,
    canvasSize: Dp = 230.dp,
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(28.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(48.dp), verticalAlignment = Alignment.CenterVertically) {
            PathPanel("before", before, viewport, VgoColors.Amber, canvasSize = canvasSize)
            AnimatedVisibility(visible = showAfter, enter = fadeIn(), exit = fadeOut()) {
                Row(horizontalArrangement = Arrangement.spacedBy(48.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("→", style = MaterialTheme.typography.h2, color = VgoColors.Muted)
                    PathPanel("after", after, viewport, VgoColors.Azure, canvasSize = canvasSize)
                }
            }
        }

        ProvideTextStyle(CodeTextStyle) {
            val current = if (showAfter) after else before
            MagicText(current.commandTokens())
        }
    }
}

@Composable
fun List<PathCommand>.commandTokens(highlightIndex: Int = -1): List<androidx.compose.ui.text.AnnotatedString> =
    toTokens(
        letterStyle = SpanStyle(color = MaterialTheme.colors.secondary),
        numberStyle = SpanStyle(color = VgoColors.OnDark),
        highlightIndex = highlightIndex,
        highlightStyle = SpanStyle(background = VgoColors.Violet.copy(alpha = 0.45f)),
    )
