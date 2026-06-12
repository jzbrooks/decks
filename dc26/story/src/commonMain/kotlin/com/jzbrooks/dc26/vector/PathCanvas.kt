package com.jzbrooks.dc26.vector

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.theme.VgoColors
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.min

/** Uniform mapping between a logical path viewport and canvas pixels. */
class ViewportTransform(val viewport: Rect, val canvasSize: Size) {
    val scale: Float = min(canvasSize.width / viewport.width, canvasSize.height / viewport.height)
    private val offsetX = (canvasSize.width - viewport.width * scale) / 2f - viewport.left * scale
    private val offsetY = (canvasSize.height - viewport.height * scale) / 2f - viewport.top * scale

    fun toScreen(point: Point) = Offset(offsetX + point.x * scale, offsetY + point.y * scale)

    fun toViewport(offset: Offset) = Point((offset.x - offsetX) / scale, (offset.y - offsetY) / scale)

    fun toScreenPath(commands: List<PathCommand>, upTo: Int = commands.size) =
        commands.toComposePath(upTo).also { path ->
            path.transform(
                Matrix().apply {
                    translate(offsetX, offsetY)
                    scale(this@ViewportTransform.scale, this@ViewportTransform.scale)
                }
            )
        }
}

fun DrawScope.drawCommands(
    transform: ViewportTransform,
    commands: List<PathCommand>,
    color: Color,
    strokeWidth: Dp = 3.dp,
    fill: Color? = null,
    upTo: Int = commands.size,
) {
    val path = transform.toScreenPath(commands, upTo)
    if (fill != null) drawPath(path, fill)
    drawPath(
        path,
        color,
        style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
    )
}

@Composable
fun PathCanvas(
    commands: List<PathCommand>,
    viewport: Rect,
    modifier: Modifier = Modifier,
    progress: Int = commands.size,
    showGrid: Boolean = true,
    showPen: Boolean = false,
    showControlPoints: Boolean = false,
    controlFilter: (Int) -> Boolean = { it == progress - 1 },
    stroke: Color = VgoColors.Azure,
    strokeWidth: Dp = 3.dp,
    fill: Color? = null,
    overlay: DrawScope.(ViewportTransform) -> Unit = {},
) {
    val steps = remember(commands) { commands.trace() }
    val penTarget = when {
        steps.isEmpty() -> Point(0f, 0f)
        progress <= 0 -> steps.first().start
        else -> steps[min(progress, steps.size) - 1].end
    }
    val penX by animateFloatAsState(penTarget.x)
    val penY by animateFloatAsState(penTarget.y)

    Canvas(modifier) {
        val transform = ViewportTransform(viewport, size)

        if (showGrid) drawGrid(transform)

        val drawn = min(progress, commands.size)
        if (drawn > 0) {
            drawCommands(transform, commands, stroke, strokeWidth, fill, drawn)
        }

        if (showControlPoints) {
            for ((index, step) in steps.withIndex()) {
                if (index >= drawn || step.controls.isEmpty() || !controlFilter(index)) continue
                drawControlHandles(transform, step)
            }
        }

        if (showPen) {
            val pen = transform.toScreen(Point(penX, penY))
            drawCircle(VgoColors.Magenta.copy(alpha = 0.25f), 13.dp.toPx(), pen)
            drawCircle(VgoColors.Magenta, 6.dp.toPx(), pen)
        }

        overlay(transform)
    }
}

private fun DrawScope.drawGrid(transform: ViewportTransform) {
    val viewport = transform.viewport
    val step = gridStep(viewport.width)

    var x = ceil(viewport.left / step) * step
    while (x <= viewport.right) {
        val color = if (abs(x) < step / 2f) VgoColors.Axis else VgoColors.GridLine
        drawLine(
            color,
            transform.toScreen(Point(x, viewport.top)),
            transform.toScreen(Point(x, viewport.bottom)),
        )
        x += step
    }

    var y = ceil(viewport.top / step) * step
    while (y <= viewport.bottom) {
        val color = if (abs(y) < step / 2f) VgoColors.Axis else VgoColors.GridLine
        drawLine(
            color,
            transform.toScreen(Point(viewport.left, y)),
            transform.toScreen(Point(viewport.right, y)),
        )
        y += step
    }
}

private fun gridStep(width: Float): Float {
    val target = width / 16f
    return listOf(0.1f, 0.2f, 0.5f, 1f, 2f, 5f, 10f, 20f, 50f, 100f)
        .minByOrNull { abs(it - target) } ?: 1f
}

private fun DrawScope.drawControlHandles(transform: ViewportTransform, step: PenStep) {
    val dash = PathEffect.dashPathEffect(floatArrayOf(8f, 8f))

    // Anchor each control to the end of the segment it pulls on.
    val anchors = when (step.command) {
        is CubicTo, is SmoothCubicTo -> listOf(step.start, step.end)
        is QuadTo -> listOf(step.start, step.end)
        else -> List(step.controls.size) { step.start }
    }

    for ((index, control) in step.controls.withIndex()) {
        val anchor = anchors.getOrElse(index) { step.start }
        val controlOffset = transform.toScreen(control)
        drawLine(
            VgoColors.Handle.copy(alpha = 0.7f),
            transform.toScreen(anchor),
            controlOffset,
            strokeWidth = 2.dp.toPx(),
            pathEffect = dash,
        )
        // Quadratic controls pull on both ends of the segment.
        if (step.command is QuadTo) {
            drawLine(
                VgoColors.Handle.copy(alpha = 0.7f),
                transform.toScreen(step.end),
                controlOffset,
                strokeWidth = 2.dp.toPx(),
                pathEffect = dash,
            )
        }
        drawCircle(VgoColors.Handle, 7.dp.toPx(), controlOffset)
        drawCircle(Color.White, 3.dp.toPx(), controlOffset)
    }
}
