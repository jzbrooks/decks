package com.jzbrooks.dc26.scenes

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.template.Caption
import com.jzbrooks.dc26.template.SlideScaffold
import com.jzbrooks.dc26.theme.VgoColors
import com.jzbrooks.dc26.vector.CubicTo
import com.jzbrooks.dc26.vector.LineTo
import com.jzbrooks.dc26.vector.MoveTo
import com.jzbrooks.dc26.vector.PathCanvas
import com.jzbrooks.dc26.vector.Point
import com.jzbrooks.dc26.vector.toCoordinateString
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.RevealEach
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import dev.bnorm.storyboard.toValue
import kotlin.math.sqrt

// Relative endpoint and control points, matching how vgo sees the command.
private val REL_END = Point(16f, 0f)
private val REL_C1 = Point(4f, -2.5f)
private val REL_C2 = Point(12f, 2.5f)
private const val TOLERANCE = 3.0f

// Absolute positions for drawing — offset to center the curve in the viewport.
private val START = Point(4f, 13f)
private val END = START + REL_END
private val C1 = START + REL_C1
private val C2 = START + REL_C2
private val VIEWPORT = Rect(0f, 0f, 24f, 24f)

// Mirror of vgo's isStraightLine(): a = −endY, b = endX, d = 1/(a²+b²).
// Distance of a relative control point from the chord = √((a·cx + b·cy)² × d).
private val A = -REL_END.y
private val B = REL_END.x
private val D = 1f / (A * A + B * B)

private fun distanceToChord(relPoint: Point): Float =
    sqrt((A * relPoint.x + B * relPoint.y) * (A * relPoint.x + B * relPoint.y) * D)

private fun footOnChord(point: Point): Point {
    val chord = END - START
    val t = ((point.x - START.x) * chord.x + (point.y - START.y) * chord.y) /
        (chord.x * chord.x + chord.y * chord.y)
    return Point(START.x + t * chord.x, START.y + t * chord.y)
}

fun StoryboardBuilder.SimplifyBezierAlgorithm() {
    scene(
        frameCount = 4,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold {
            val frame = transition.currentState.toValue()
            val d1 = distanceToChord(REL_C1)
            val d2 = distanceToChord(REL_C2)
            val commands = if (frame >= 3) {
                listOf(MoveTo(START), LineTo(END))
            } else {
                listOf(MoveTo(START), CubicTo(C1, C2, END))
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(96.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PathCanvas(
                    commands = commands,
                    viewport = VIEWPORT,
                    showControlPoints = false,
                    stroke = if (frame >= 3) VgoColors.Azure else VgoColors.Amber,
                    modifier = Modifier
                        .size(680.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(VgoColors.Surface),
                ) { transform ->
                    val dotRadius = 10.dp.toPx()
                    val lineWidth = 4.dp.toPx()

                    if (frame >= 1) {
                        val sStart = transform.toScreen(START)
                        val sEnd = transform.toScreen(END)
                        drawLine(
                            VgoColors.Azure,
                            Offset(sStart.x, sStart.y),
                            Offset(sEnd.x, sEnd.y),
                            lineWidth,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f)),
                        )

                        // Chord normal arrow from midpoint, direction (a, b) normalised.
                        val normalMag = sqrt(A * A + B * B)
                        val normalScale = 3f / normalMag  // 3 viewport units long
                        val mid = Point(START.x + REL_END.x * 0.5f, START.y + REL_END.y * 0.5f)
                        val normalTip = Point(mid.x + A * normalScale, mid.y + B * normalScale)
                        val sMid = transform.toScreen(mid)
                        val sNormalTip = transform.toScreen(normalTip)
                        drawLine(VgoColors.Azure, Offset(sMid.x, sMid.y), Offset(sNormalTip.x, sNormalTip.y), lineWidth)
                        drawCircle(VgoColors.Azure, dotRadius * 0.7f, Offset(sNormalTip.x, sNormalTip.y))
                    }

                    if (frame >= 2) {
                        val sc1 = transform.toScreen(C1)
                        val foot1 = transform.toScreen(footOnChord(C1))
                        drawLine(VgoColors.Magenta, Offset(sc1.x, sc1.y), Offset(foot1.x, foot1.y), lineWidth)
                        drawCircle(VgoColors.Amber, dotRadius, Offset(sc1.x, sc1.y))
                        drawCircle(VgoColors.Magenta, dotRadius * 0.6f, Offset(foot1.x, foot1.y))
                    }

                    if (frame >= 3) {
                        val sc2 = transform.toScreen(C2)
                        val foot2 = transform.toScreen(footOnChord(C2))
                        drawLine(VgoColors.Magenta, Offset(sc2.x, sc2.y), Offset(foot2.x, foot2.y), lineWidth)
                        drawCircle(VgoColors.Amber, dotRadius, Offset(sc2.x, sc2.y))
                        drawCircle(VgoColors.Magenta, dotRadius * 0.6f, Offset(foot2.x, foot2.y))
                    }
                }

                Column(
                    Modifier.width(840.dp).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                ) {
                    Text("how it works", style = MaterialTheme.typography.h4)

                    RevealEach(transition.createChildTransition { it.toValue() }) {
                        item(0) {
                            BezierAlgorithmStep(
                                label = "1. draw the chord",
                                detail = "In relative coordinates the chord runs from the origin to end. " +
                                    "Rotating it 90° gives the chord normal (a, b) = (−endY, endX).",
                            )
                        }
                        item(1) {
                            BezierAlgorithmStep(
                                label = "2. normalise by chord length",
                                detail = "d = 1 / (a² + b²). " +
                                    "If d is not finite the chord is zero-length — vgo skips the check.",
                            )
                        }
                        item(2) {
                            BezierAlgorithmStep(
                                label = "3. project each control point",
                                detail = "distance = √((a·cx + b·cy)² × d). " +
                                    "C1 ≈ ${d1.toCoordinateString(2)}, C2 ≈ ${d2.toCoordinateString(2)} " +
                                    "— both ≤ ${TOLERANCE.toCoordinateString(2)}.",
                            )
                        }
                        item(3) {
                            BezierAlgorithmStep(
                                label = "4. replace with LineTo",
                                detail = "Both controls project within tolerance: the cubic is swapped " +
                                    "for a straight line and the command is shorter.",
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BezierAlgorithmStep(label: String, detail: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = MaterialTheme.typography.subtitle1, color = VgoColors.Azure)
        Caption(detail)
    }
}
