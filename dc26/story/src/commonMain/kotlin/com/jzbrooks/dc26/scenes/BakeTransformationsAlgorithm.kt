package com.jzbrooks.dc26.scenes

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.template.AlgorithmStep
import com.jzbrooks.dc26.template.SlideScaffold
import com.jzbrooks.dc26.theme.VgoColors
import com.jzbrooks.dc26.vector.Close
import com.jzbrooks.dc26.vector.LineTo
import com.jzbrooks.dc26.vector.MoveTo
import com.jzbrooks.dc26.vector.PathCanvas
import com.jzbrooks.dc26.vector.PathCommand
import com.jzbrooks.dc26.vector.Point
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.RevealEach
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import dev.bnorm.storyboard.toValue
import kotlin.math.sqrt
import androidx.compose.ui.graphics.Path as DrawPath

// The rectangle from the previous slide: M6,12h12v4h-12z rotated 45° about (12,12).
private val ORIGINAL: List<PathCommand> =
    listOf(
        MoveTo(Point(6f, 12f)),
        LineTo(Point(18f, 12f)),
        LineTo(Point(18f, 16f)),
        LineTo(Point(6f, 16f)),
        Close,
    )

private val BAKED: List<PathCommand> =
    listOf(
        MoveTo(Point(7.76f, 7.76f)),
        LineTo(Point(16.24f, 16.24f)),
        LineTo(Point(13.41f, 19.07f)),
        LineTo(Point(4.93f, 10.59f)),
        Close,
    )

private val PIVOT = Point(12f, 12f)
private val VIEWPORT = Rect(0f, 0f, 24f, 24f)

// Each original corner and its image under M — p′ = M · (x, y, 1).
private val CORNER_PAIRS =
    listOf(
        Point(6f, 12f) to Point(7.76f, 7.76f),
        Point(18f, 12f) to Point(16.24f, 16.24f),
        Point(18f, 16f) to Point(13.41f, 19.07f),
        Point(6f, 16f) to Point(4.93f, 10.59f),
    )

fun StoryboardBuilder.bakeTransformationsAlgorithm() {
    scene(
        frameCount = 4,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold(badge = "Top-down", badgeLabel = "Bake Transformations") {
            val frame = transition.currentState.toValue()

            Row(
                horizontalArrangement = Arrangement.spacedBy(96.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PathCanvas(
                    commands = if (frame >= 1) BAKED else ORIGINAL,
                    viewport = VIEWPORT,
                    fill = VgoColors.PathFill,
                    stroke = if (frame >= 1) VgoColors.Azure else VgoColors.Amber,
                    modifier =
                        Modifier
                            .size(680.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .background(VgoColors.Surface),
                ) { transform ->
                    val dotRadius = 10.dp.toPx()
                    val lineWidth = 4.dp.toPx()
                    val dash = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)

                    val sPivot = transform.toScreen(PIVOT)
                    drawCircle(VgoColors.Azure, dotRadius, Offset(sPivot.x, sPivot.y))

                    // Frame 1+: ghost of the untransformed rect and each corner's
                    // journey through the matrix.
                    if (frame >= 1) {
                        val ghost =
                            DrawPath().apply {
                                CORNER_PAIRS.forEachIndexed { i, (original, _) ->
                                    val sp = transform.toScreen(original)
                                    if (i == 0) moveTo(sp.x, sp.y) else lineTo(sp.x, sp.y)
                                }
                                close()
                            }
                        drawPath(ghost, VgoColors.Amber, style = Stroke(lineWidth, pathEffect = dash))

                        for ((original, mapped) in CORNER_PAIRS) {
                            val from = transform.toScreen(original)
                            val to = transform.toScreen(mapped)
                            drawLine(VgoColors.Magenta, Offset(from.x, from.y), Offset(to.x, to.y), lineWidth, pathEffect = dash)
                            drawCircle(VgoColors.Amber, dotRadius * 0.6f, Offset(from.x, from.y))
                            drawCircle(VgoColors.Magenta, dotRadius * 0.6f, Offset(to.x, to.y))
                        }
                    }

                    // Frame 2+: an arc's ellipse — the circle survives a rotation,
                    // but its axes ride along with the transform.
                    if (frame >= 2) {
                        val radius = 4f * transform.scale
                        drawCircle(
                            VgoColors.Muted,
                            radius,
                            Offset(sPivot.x, sPivot.y),
                            style = Stroke(lineWidth, pathEffect = dash),
                        )

                        // Rotated major/minor axes (45°/135°).
                        val d = 4f / sqrt(2f) * transform.scale
                        drawLine(
                            VgoColors.Azure,
                            Offset(sPivot.x - d, sPivot.y - d),
                            Offset(sPivot.x + d, sPivot.y + d),
                            lineWidth,
                        )
                        drawLine(
                            VgoColors.Azure,
                            Offset(sPivot.x - d, sPivot.y + d),
                            Offset(sPivot.x + d, sPivot.y - d),
                            lineWidth,
                        )
                    }
                }

                Column(
                    Modifier.weight(1f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                ) {
                    Text("how it works", style = MaterialTheme.typography.h4)

                    RevealEach(transition.createChildTransition { it.toValue() }) {
                        item(0) {
                            AlgorithmStep(
                                label = "1. compose one matrix",
                                detail =
                                    "Rotation, pivot, scale, and translation collapse into one " +
                                        "3×3 matrix M — baked only when every child is a path or group.",
                            )
                        }
                        item(1) {
                            AlgorithmStep(
                                label = "2. resolve, then multiply",
                                detail =
                                    "Each coordinate is made absolute, then multiplied through M. " +
                                        "H/V shorthands become full lines — a rotated line needs both axes.",
                            )
                        }
                        item(2) {
                            AlgorithmStep(
                                label = "3. arcs store shape, not points",
                                detail =
                                    "Radii and angle describe an ellipse, but a matrix only moves " +
                                        "points. vgo pushes the whole ellipse through M and measures what " +
                                        "comes out — new radii and axis; a mirror flips the sweep.",
                            )
                        }
                        item(3) {
                            AlgorithmStep(
                                label = "4. push down and zero out",
                                detail =
                                    "Nested groups pre-multiply (M × child) and bake later in the " +
                                        "pass. The group's transform resets to identity — empty group left behind.",
                            )
                        }
                    }
                }
            }
        }
    }
}
