package com.jzbrooks.dc26.scenes

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.template.Caption
import com.jzbrooks.dc26.template.CodeTextStyle
import com.jzbrooks.dc26.template.OutlinedChip
import com.jzbrooks.dc26.template.SlideScaffold
import com.jzbrooks.dc26.theme.VgoColors
import com.jzbrooks.dc26.vector.CubicTo
import com.jzbrooks.dc26.vector.LineTo
import com.jzbrooks.dc26.vector.MoveTo
import com.jzbrooks.dc26.vector.PathCanvas
import com.jzbrooks.dc26.vector.Point
import com.jzbrooks.dc26.vector.commandTokens
import com.jzbrooks.dc26.vector.dragHandles
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.RevealEach
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.toValue
import kotlin.math.abs
import kotlin.math.sqrt

private val START = Point(4f, 12f)
private val END = Point(20f, 12f)
private val VIEWPORT = Rect(0f, 0f, 24f, 24f)
private const val TOLERANCE = 0.45f

private fun distanceToChord(point: Point): Float {
    val chord = END - START
    val toPoint = point - START
    val cross = chord.x * toPoint.y - chord.y * toPoint.x
    return abs(cross) / sqrt(chord.x * chord.x + chord.y * chord.y)
}

fun StoryboardBuilder.SimplifyCommands() {
    scene(
        frameCount = 4,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold {
            Column(verticalArrangement = Arrangement.spacedBy(48.dp)) {
                InteractiveDegenerateCurve()

                RevealEach(transition.createChildTransition { it.toValue() }) {
                    item(index = 1) {
                        SimplifyRow(
                            pass = "matching controls → S",
                            before = "c4,-6,12,-6,16,0c4,6,12,6,16,0",
                            after = "c4,-6,12,-6,16,0s12,6,16,0",
                        )
                    }
                    item(index = 2) {
                        SimplifyRow(
                            pass = "axis-aligned → H/V, collinear merged",
                            before = "l8,0l6,0l0,5",
                            after = "h14v5",
                        )
                    }
                    item(index = 3) {
                        SimplifyRow(
                            pass = "zero-length → gone",
                            before = "l5,3l0,0l2,4",
                            after = "l5,3,2,4",
                        )
                    }
                }
            }
        }
    }
}

@androidx.compose.runtime.Composable
private fun InteractiveDegenerateCurve() {
    var control1 by remember { mutableStateOf(Point(9f, 5f)) }
    var control2 by remember { mutableStateOf(Point(15f, 19f)) }

    val degenerate = distanceToChord(control1) < TOLERANCE && distanceToChord(control2) < TOLERANCE
    val commands = if (degenerate) {
        listOf(MoveTo(START), LineTo(END))
    } else {
        listOf(MoveTo(START), CubicTo(control1, control2, END))
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(80.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PathCanvas(
            commands = listOf(MoveTo(START), CubicTo(control1, control2, END)),
            viewport = VIEWPORT,
            showControlPoints = true,
            controlFilter = { true },
            stroke = if (degenerate) VgoColors.Azure else VgoColors.Amber,
            modifier = Modifier
                .width(520.dp)
                .height(520.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(VgoColors.Surface)
                .dragHandles(VIEWPORT, { listOf(control1, control2) }) { index, position ->
                    if (index == 0) control1 = position else control2 = position
                },
        )

        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            Text("SimplifyBezierCurveCommands", style = MaterialTheme.typography.h4)
            ProvideTextStyle(CodeTextStyle) {
                MagicText(commands.commandTokens())
            }
            Caption(
                if (degenerate) "control points sit on the chord — it was never a curve"
                else "drag the control points onto the line between the endpoints"
            )
        }
    }
}

@androidx.compose.runtime.Composable
private fun SimplifyRow(pass: String, before: String, after: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedChip(pass, color = VgoColors.Muted)
        ProvideTextStyle(CodeTextStyle) {
            Text(before, color = VgoColors.Amber)
            Text("→", color = VgoColors.Muted)
            Text(after, color = VgoColors.Azure)
        }
    }
}
