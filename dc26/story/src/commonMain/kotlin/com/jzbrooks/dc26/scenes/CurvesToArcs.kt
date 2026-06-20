package com.jzbrooks.dc26.scenes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.template.ByteChip
import com.jzbrooks.dc26.template.Caption
import com.jzbrooks.dc26.template.CodeTextStyle
import com.jzbrooks.dc26.template.OutlinedChip
import com.jzbrooks.dc26.template.SlideScaffold
import com.jzbrooks.dc26.theme.VgoColors
import com.jzbrooks.dc26.vector.ArcTo
import com.jzbrooks.dc26.vector.CubicTo
import com.jzbrooks.dc26.vector.MoveTo
import com.jzbrooks.dc26.vector.PathCanvas
import com.jzbrooks.dc26.vector.Point
import com.jzbrooks.dc26.vector.dragHandles
import com.jzbrooks.dc26.vector.toCoordinateString
import com.jzbrooks.dc26.vector.toSvgText
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import dev.bnorm.storyboard.toValue
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.max
import kotlin.math.sqrt

private val START = Point(4f, 18f)
private val END = Point(20f, 18f)
private val VIEWPORT = Rect(0f, 0f, 24f, 24f)
private const val TOLERANCE = 0.35f

private class FittedCircle(val center: Point, val radius: Float, val maxError: Float)

private fun cubicPoint(c1: Point, c2: Point, t: Float): Point {
    val u = 1f - t
    val x = u * u * u * START.x + 3f * u * u * t * c1.x + 3f * u * t * t * c2.x + t * t * t * END.x
    val y = u * u * u * START.y + 3f * u * u * t * c1.y + 3f * u * t * t * c2.y + t * t * t * END.y
    return Point(x, y)
}

private fun fitCircle(c1: Point, c2: Point): FittedCircle? {
    val mid = cubicPoint(c1, c2, 0.5f)
    val (ax, ay) = START
    val (bx, by) = mid
    val (cx, cy) = END

    val d = 2f * (ax * (by - cy) + bx * (cy - ay) + cx * (ay - by))
    if (abs(d) < 1e-4f) return null

    val a2 = ax * ax + ay * ay
    val b2 = bx * bx + by * by
    val c2sq = cx * cx + cy * cy
    val ux = (a2 * (by - cy) + b2 * (cy - ay) + c2sq * (ay - by)) / d
    val uy = (a2 * (cx - bx) + b2 * (ax - cx) + c2sq * (bx - ax)) / d
    val center = Point(ux, uy)

    val dx = ax - ux
    val dy = ay - uy
    val radius = sqrt(dx * dx + dy * dy)

    var maxError = 0f
    for (i in 0..24) {
        val sample = cubicPoint(c1, c2, i / 24f)
        val sx = sample.x - ux
        val sy = sample.y - uy
        maxError = max(maxError, abs(sqrt(sx * sx + sy * sy) - radius))
    }
    return FittedCircle(center, radius, maxError)
}

private fun arcSweepDegrees(circle: FittedCircle, c1: Point, c2: Point): Pair<Float, Float> {
    fun angleOf(p: Point) =
        (atan2(p.y - circle.center.y, p.x - circle.center.x) * 180f / PI.toFloat() + 360f) % 360f

    val startAngle = angleOf(START)
    val endAngle = angleOf(END)
    val midAngle = angleOf(cubicPoint(c1, c2, 0.5f))

    val forward = (endAngle - startAngle + 360f) % 360f
    val midOffset = (midAngle - startAngle + 360f) % 360f
    val sweep = if (midOffset <= forward) forward else forward - 360f
    return startAngle to sweep
}

fun StoryboardBuilder.CurvesToArcs() {
    scene(
        frameCount = 3,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold {
            val frame = transition.currentState.toValue()
            var control1 by remember { mutableStateOf(Point(8f, 6f)) }
            var control2 by remember { mutableStateOf(Point(16f, 6f)) }

            val cubic = CubicTo(control1, control2, END)
            val circle = fitCircle(control1, control2)
            val arc = circle?.let {
                val (_, sweep) = arcSweepDegrees(it, control1, control2)
                ArcTo(
                    rx = it.radius,
                    ry = it.radius,
                    rotation = 0f,
                    largeArc = abs(sweep) > 180f,
                    sweep = sweep > 0f,
                    to = END,
                )
            }

            val cubicText = cubic.toSvgText(precision = 1)
            val arcText = arc?.toSvgText(precision = 1)
            val withinTolerance = circle != null && circle.maxError < TOLERANCE
            val shorter = arcText != null && arcText.length < cubicText.length
            val converts = withinTolerance && shorter

            Row(
                horizontalArrangement = Arrangement.spacedBy(96.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PathCanvas(
                    commands = listOf(MoveTo(START), cubic),
                    viewport = VIEWPORT,
                    showControlPoints = true,
                    controlFilter = { true },
                    stroke = VgoColors.Amber,
                    modifier = Modifier
                        .size(680.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(VgoColors.Surface)
                        .dragHandles(VIEWPORT, { listOf(control1, control2) }) { index, position ->
                            if (index == 0) control1 = position else control2 = position
                        },
                ) { transform ->
                    if (circle != null) {
                        val (startAngle, sweep) = arcSweepDegrees(circle, control1, control2)
                        val center = transform.toScreen(circle.center)
                        val radius = circle.radius * transform.scale
                        drawArc(
                            color = VgoColors.Azure,
                            startAngle = startAngle,
                            sweepAngle = sweep,
                            useCenter = false,
                            topLeft = Offset(center.x - radius, center.y - radius),
                            size = Size(radius * 2f, radius * 2f),
                            style = Stroke(6.dp.toPx()),
                        )
                    }
                }

                Column(Modifier.width(840.dp), verticalArrangement = Arrangement.spacedBy(32.dp)) {
                    Text("ConvertCurvesToArcs", style = MaterialTheme.typography.h4)

                    ProvideTextStyle(CodeTextStyle) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(24.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(cubicText, color = VgoColors.Amber)
                                if (frame >= 1) ByteChip(cubicText.length, color = VgoColors.Amber)
                            }
                            if (arcText != null) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(arcText, color = VgoColors.Azure)
                                    if (frame >= 1) ByteChip(arcText.length)
                                }
                            }
                        }
                    }

                    if (circle != null) {
                        OutlinedChip(
                            "max error ≈ ${circle.maxError.toCoordinateString(2)}",
                            color = if (withinTolerance) VgoColors.Azure else VgoColors.Amber,
                        )
                    }

                    when {
                        frame >= 2 -> Caption(
                            if (converts) "shorter and faithful — vgo converts ✓"
                            else "not worth it here — vgo keeps the curve ✗"
                        )

                        else -> Caption("drag the control points — the fitted arc follows")
                    }
                }
            }
        }
    }
}
