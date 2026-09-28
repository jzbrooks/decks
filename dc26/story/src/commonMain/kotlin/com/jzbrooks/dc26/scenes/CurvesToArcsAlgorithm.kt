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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.template.AlgorithmStep
import com.jzbrooks.dc26.template.SlideScaffold
import com.jzbrooks.dc26.theme.VgoColors
import com.jzbrooks.dc26.vector.CubicTo
import com.jzbrooks.dc26.vector.MoveTo
import com.jzbrooks.dc26.vector.PathCanvas
import com.jzbrooks.dc26.vector.Point
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.RevealEach
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
private val C1 = Point(8f, 6f)
private val C2 = Point(16f, 6f)

private class FittedArc(
    val center: Point,
    val radius: Float,
    val maxError: Float,
)

private fun cubicPoint(t: Float): Point {
    val u = 1f - t
    return Point(
        u * u * u * START.x + 3f * u * u * t * C1.x + 3f * u * t * t * C2.x + t * t * t * END.x,
        u * u * u * START.y + 3f * u * u * t * C1.y + 3f * u * t * t * C2.y + t * t * t * END.y,
    )
}

private fun fitArc(): FittedArc? {
    val mid = cubicPoint(0.5f)
    val (ax, ay) = START
    val (bx, by) = mid
    val (cx, cy) = END

    val d = 2f * (ax * (by - cy) + bx * (cy - ay) + cx * (ay - by))
    if (abs(d) < 1e-4f) return null

    val a2 = ax * ax + ay * ay
    val b2 = bx * bx + by * by
    val c2 = cx * cx + cy * cy
    val ux = (a2 * (by - cy) + b2 * (cy - ay) + c2 * (ay - by)) / d
    val uy = (a2 * (cx - bx) + b2 * (ax - cx) + c2 * (bx - ax)) / d
    val center = Point(ux, uy)

    val radius = sqrt((ax - ux) * (ax - ux) + (ay - uy) * (ay - uy))

    var maxError = 0f
    for (i in 0..24) {
        val s = cubicPoint(i / 24f)
        maxError = max(maxError, abs(sqrt((s.x - ux) * (s.x - ux) + (s.y - uy) * (s.y - uy)) - radius))
    }
    return FittedArc(center, radius, maxError)
}

private fun arcAngles(arc: FittedArc): Pair<Float, Float> {
    fun angleOf(p: Point) = (atan2(p.y - arc.center.y, p.x - arc.center.x) * 180f / PI.toFloat() + 360f) % 360f

    val startAngle = angleOf(START)
    val endAngle = angleOf(END)
    val midAngle = angleOf(cubicPoint(0.5f))
    val forward = (endAngle - startAngle + 360f) % 360f
    val midOffset = (midAngle - startAngle + 360f) % 360f
    val sweep = if (midOffset <= forward) forward else forward - 360f
    return startAngle to sweep
}

private fun bisectorEndpoints(
    a: Point,
    b: Point,
    halfLen: Float,
): Pair<Point, Point> {
    val mx = (a.x + b.x) / 2f
    val my = (a.y + b.y) / 2f
    val dx = b.x - a.x
    val dy = b.y - a.y
    val mag = sqrt(dx * dx + dy * dy)
    val px = -dy / mag
    val py = dx / mag
    return Point(mx + px * halfLen, my + py * halfLen) to Point(mx - px * halfLen, my - py * halfLen)
}

fun StoryboardBuilder.curvesToArcsAlgorithm() {
    scene(
        frameCount = 4,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold(badge = "Top-down", badgeLabel = "Convert Curves To Arcs") {
            val frame = transition.currentState.toValue()

            val mid = cubicPoint(0.5f)
            val q1 = cubicPoint(0.25f)
            val q3 = cubicPoint(0.75f)
            val arc = fitArc()

            Row(
                horizontalArrangement = Arrangement.spacedBy(96.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PathCanvas(
                    commands = listOf(MoveTo(START), CubicTo(C1, C2, END)),
                    viewport = VIEWPORT,
                    showControlPoints = false,
                    stroke = VgoColors.Amber,
                    modifier =
                        Modifier
                            .size(680.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .background(VgoColors.Surface),
                ) { transform ->
                    val dotRadius = 10.dp.toPx()

                    if (frame >= 1) {
                        val sq1 = transform.toScreen(q1)
                        val smid = transform.toScreen(mid)
                        val sq3 = transform.toScreen(q3)
                        drawCircle(VgoColors.Magenta, dotRadius, Offset(sq1.x, sq1.y))
                        drawCircle(VgoColors.Azure, dotRadius * 1.3f, Offset(smid.x, smid.y))
                        drawCircle(VgoColors.Magenta, dotRadius, Offset(sq3.x, sq3.y))
                    }

                    if (frame >= 2 && arc != null) {
                        val dashes = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
                        val lineWidth = 4.dp.toPx()

                        val (b1a, b1b) = bisectorEndpoints(START, mid, 7f)
                        val sb1a = transform.toScreen(b1a)
                        val sb1b = transform.toScreen(b1b)
                        drawLine(VgoColors.Azure, Offset(sb1a.x, sb1a.y), Offset(sb1b.x, sb1b.y), lineWidth, pathEffect = dashes)

                        val (b2a, b2b) = bisectorEndpoints(mid, END, 7f)
                        val sb2a = transform.toScreen(b2a)
                        val sb2b = transform.toScreen(b2b)
                        drawLine(VgoColors.Azure, Offset(sb2a.x, sb2a.y), Offset(sb2b.x, sb2b.y), lineWidth, pathEffect = dashes)

                        val sc = transform.toScreen(arc.center)
                        drawCircle(VgoColors.Azure, dotRadius, Offset(sc.x, sc.y))

                        val (startAngle, sweep) = arcAngles(arc)
                        val radius = arc.radius * transform.scale
                        drawArc(
                            color = VgoColors.Azure,
                            startAngle = startAngle,
                            sweepAngle = sweep,
                            useCenter = false,
                            topLeft = Offset(sc.x - radius, sc.y - radius),
                            size = Size(radius * 2f, radius * 2f),
                            style = Stroke(6.dp.toPx()),
                        )
                    }

                    if (frame >= 3 && arc != null) {
                        val sc = transform.toScreen(arc.center)
                        val radius = arc.radius * transform.scale
                        for (sample in listOf(q1, q3)) {
                            val sp = transform.toScreen(sample)
                            val dx = sp.x - sc.x
                            val dy = sp.y - sc.y
                            val dist = sqrt(dx * dx + dy * dy)
                            val edge = Offset(sc.x + dx / dist * radius, sc.y + dy / dist * radius)
                            drawLine(VgoColors.Azure, Offset(sp.x, sp.y), edge, 4.dp.toPx())
                        }
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
                                label = "1. sample the curve",
                                detail =
                                    "Evaluate the cubic Bézier at t = 0.5 to get the midpoint. " +
                                        "t = 0.25 and t = 0.75 are sampled later for verification.",
                            )
                        }
                        item(1) {
                            AlgorithmStep(
                                label = "2. fit a circle",
                                detail =
                                    "Perpendicular bisectors of start→mid and mid→end chords " +
                                        "intersect at the circle center. Radius = distance from center to start.",
                            )
                        }
                        item(2) {
                            AlgorithmStep(
                                label = "3. verify the fit",
                                detail =
                                    "Samples at t = 0.25 and t = 0.75 must each lie within " +
                                        "tolerance (≈ 2 × 10⁻³) of the fitted radius.",
                            )
                        }
                        item(3) {
                            AlgorithmStep(
                                label = "4. convert if shorter",
                                detail =
                                    "Serialize both commands; replace the curve only when " +
                                        "the arc text is strictly shorter.",
                            )
                        }
                    }
                }
            }
        }
    }
}
