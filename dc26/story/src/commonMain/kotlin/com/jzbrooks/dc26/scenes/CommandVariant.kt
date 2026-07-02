package com.jzbrooks.dc26.scenes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.template.ByteChip
import com.jzbrooks.dc26.template.Caption
import com.jzbrooks.dc26.template.CodeTextStyle
import com.jzbrooks.dc26.template.SlideScaffold
import com.jzbrooks.dc26.theme.VgoColors
import com.jzbrooks.dc26.vector.LineTo
import com.jzbrooks.dc26.vector.MoveTo
import com.jzbrooks.dc26.vector.PathCanvas
import com.jzbrooks.dc26.vector.PathCommand
import com.jzbrooks.dc26.vector.Point
import com.jzbrooks.dc26.vector.byteCount
import com.jzbrooks.dc26.vector.commandTokens
import com.jzbrooks.dc26.vector.toAbsolute
import com.jzbrooks.dc26.vector.toCompact
import com.jzbrooks.dc26.vector.toRelative
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.toValue

private enum class Variant { Absolute, Relative, Compact }

private val ZIGZAG: List<PathCommand> = listOf(
    MoveTo(Point(10f, 10f)),
    LineTo(Point(90f, 10f)),
    LineTo(Point(10f, 90f)),
    LineTo(Point(90f, 90f)),
)

fun StoryboardBuilder.CommandVariant() {
    scene(
        frameCount = 3,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold(badge = "Top-down", badgeLabel = "Command Variant") {
            val frame = transition.currentState.toValue()
            var selection by remember { mutableStateOf<Variant?>(null) }
            // Frames walk the variants for the talk; the buttons let the speaker go off-script.
            val variant = selection ?: Variant.entries[frame.coerceIn(0, Variant.entries.lastIndex)]

            val commands = when (variant) {
                Variant.Absolute -> ZIGZAG.toAbsolute()
                Variant.Relative -> ZIGZAG.toRelative()
                Variant.Compact -> ZIGZAG.toCompact()
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(96.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PathCanvas(
                    commands = commands,
                    viewport = Rect(0f, 0f, 100f, 100f),
                    fill = VgoColors.PathFill,
                    modifier = Modifier
                        .size(640.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(VgoColors.Surface),
                )

                Column(verticalArrangement = Arrangement.spacedBy(48.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        for (candidate in Variant.entries) {
                            VariantButton(
                                candidate,
                                selected = candidate == variant,
                                onClick = { selection = candidate },
                            )
                        }
                    }

                    ProvideTextStyle(CodeTextStyle) {
                        MagicText(commands.commandTokens())
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ByteChip(commands.byteCount())
                        Caption(
                            when (variant) {
                                Variant.Absolute -> "every coordinate measured from the origin"
                                Variant.Relative -> "every coordinate measured from the pen"
                                Variant.Compact -> "per command, whichever spelling is shorter"
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VariantButton(variant: Variant, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (selected) VgoColors.Violet else VgoColors.Surface,
            contentColor = if (selected) androidx.compose.ui.graphics.Color.White else VgoColors.OnDark,
        ),
        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
    ) {
        Text(variant.name, style = MaterialTheme.typography.h4)
    }
}
