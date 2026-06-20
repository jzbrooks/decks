package com.jzbrooks.dc26.vector

import androidx.compose.ui.graphics.Path
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * One command resolved against the pen state: everything is in absolute coordinates,
 * regardless of how the command itself was written.
 */
class PenStep(
    val command: PathCommand,
    val start: Point,
    val end: Point,
    /** Absolute control points; for smooth curves the first entry is the implied (reflected) control. */
    val controls: List<Point>,
    val subpathStart: Point,
)

fun List<PathCommand>.trace(): List<PenStep> {
    var pen = Point(0f, 0f)
    var subpathStart = pen
    var previousCubicControl: Point? = null

    fun Point.abs(origin: Point, relative: Boolean) = if (relative) origin + this else this

    val steps = mutableListOf<PenStep>()
    for (command in this) {
        val start = pen
        var controls = emptyList<Point>()
        var nextCubicControl: Point? = null

        when (command) {
            is MoveTo -> {
                pen = command.to.abs(start, command.relative)
                subpathStart = pen
            }

            is LineTo -> pen = command.to.abs(start, command.relative)

            is HorizontalTo -> {
                val x = if (command.relative) start.x + command.x else command.x
                pen = Point(x, start.y)
            }

            is VerticalTo -> {
                val y = if (command.relative) start.y + command.y else command.y
                pen = Point(start.x, y)
            }

            is CubicTo -> {
                val c1 = command.control1.abs(start, command.relative)
                val c2 = command.control2.abs(start, command.relative)
                pen = command.to.abs(start, command.relative)
                controls = listOf(c1, c2)
                nextCubicControl = c2
            }

            is SmoothCubicTo -> {
                val previous = previousCubicControl
                val c1 = if (previous != null) start + (start - previous) else start
                val c2 = command.control2.abs(start, command.relative)
                pen = command.to.abs(start, command.relative)
                controls = listOf(c1, c2)
                nextCubicControl = c2
            }

            is QuadTo -> {
                val c = command.control.abs(start, command.relative)
                pen = command.to.abs(start, command.relative)
                controls = listOf(c)
            }

            is ArcTo -> pen = command.to.abs(start, command.relative)

            Close -> pen = subpathStart
        }

        previousCubicControl = nextCubicControl
        steps += PenStep(command, start, pen, controls, subpathStart)
    }
    return steps
}

fun List<PathCommand>.toComposePath(upTo: Int = size): Path {
    val path = Path()
    val steps = trace()
    for (step in steps.take(min(upTo, steps.size))) {
        when (val command = step.command) {
            is MoveTo -> path.moveTo(step.end.x, step.end.y)
            is LineTo, is HorizontalTo, is VerticalTo -> path.lineTo(step.end.x, step.end.y)

            is CubicTo, is SmoothCubicTo -> {
                val (c1, c2) = step.controls
                path.cubicTo(c1.x, c1.y, c2.x, c2.y, step.end.x, step.end.y)
            }

            is QuadTo -> {
                val c = step.controls.single()
                path.quadraticTo(c.x, c.y, step.end.x, step.end.y)
            }

            is ArcTo -> path.addSvgArc(step.start, step.end, command)

            Close -> path.close()
        }
    }
    return path
}

private class CenterArc(
    val cx: Float,
    val cy: Float,
    val rx: Float,
    val ry: Float,
    val startAngle: Float, // degrees
    val sweepAngle: Float, // degrees
)

private fun Path.addSvgArc(start: Point, end: Point, command: ArcTo) {
    val arc = endpointToCenter(start, end, command) ?: run {
        lineTo(end.x, end.y)
        return
    }

    if (command.rotation == 0f) {
        arcTo(
            rect = androidx.compose.ui.geometry.Rect(
                left = arc.cx - arc.rx,
                top = arc.cy - arc.ry,
                right = arc.cx + arc.rx,
                bottom = arc.cy + arc.ry,
            ),
            startAngleDegrees = arc.startAngle,
            sweepAngleDegrees = arc.sweepAngle,
            forceMoveTo = false,
        )
    } else {
        // Rotated ellipses only appear in edge-case demos; flatten them.
        val phi = command.rotation * PI.toFloat() / 180f
        val steps = 64
        for (i in 1..steps) {
            val angle = (arc.startAngle + arc.sweepAngle * i / steps) * PI.toFloat() / 180f
            val x = arc.rx * cos(angle)
            val y = arc.ry * sin(angle)
            lineTo(
                arc.cx + x * cos(phi) - y * sin(phi),
                arc.cy + x * sin(phi) + y * cos(phi),
            )
        }
    }
}

// SVG 1.1 spec F.6.5: conversion from endpoint to center parameterization.
private fun endpointToCenter(start: Point, end: Point, command: ArcTo): CenterArc? {
    var rx = abs(command.rx)
    var ry = abs(command.ry)
    if (rx == 0f || ry == 0f || (start.x == end.x && start.y == end.y)) return null

    val phi = command.rotation * PI.toFloat() / 180f
    val cosPhi = cos(phi)
    val sinPhi = sin(phi)

    val dx2 = (start.x - end.x) / 2f
    val dy2 = (start.y - end.y) / 2f
    val x1p = cosPhi * dx2 + sinPhi * dy2
    val y1p = -sinPhi * dx2 + cosPhi * dy2

    val lambda = (x1p * x1p) / (rx * rx) + (y1p * y1p) / (ry * ry)
    if (lambda > 1f) {
        val scale = sqrt(lambda)
        rx *= scale
        ry *= scale
    }

    val sign = if (command.largeArc != command.sweep) 1f else -1f
    val numerator = (rx * rx * ry * ry - rx * rx * y1p * y1p - ry * ry * x1p * x1p).coerceAtLeast(0f)
    val denominator = rx * rx * y1p * y1p + ry * ry * x1p * x1p
    val coefficient = sign * sqrt(numerator / denominator)

    val cxp = coefficient * rx * y1p / ry
    val cyp = -coefficient * ry * x1p / rx

    val cx = cosPhi * cxp - sinPhi * cyp + (start.x + end.x) / 2f
    val cy = sinPhi * cxp + cosPhi * cyp + (start.y + end.y) / 2f

    fun angleBetween(ux: Float, uy: Float, vx: Float, vy: Float): Float {
        val dot = ux * vx + uy * vy
        val length = sqrt((ux * ux + uy * uy) * (vx * vx + vy * vy))
        var angle = acos((dot / length).coerceIn(-1f, 1f))
        if (ux * vy - uy * vx < 0f) angle = -angle
        return angle
    }

    val startAngle = angleBetween(1f, 0f, (x1p - cxp) / rx, (y1p - cyp) / ry)
    var sweepAngle = angleBetween(
        (x1p - cxp) / rx, (y1p - cyp) / ry,
        (-x1p - cxp) / rx, (-y1p - cyp) / ry,
    )
    val tau = 2f * PI.toFloat()
    if (!command.sweep && sweepAngle > 0f) sweepAngle -= tau
    if (command.sweep && sweepAngle < 0f) sweepAngle += tau

    val toDegrees = 180f / PI.toFloat()
    return CenterArc(cx, cy, rx, ry, startAngle * toDegrees, sweepAngle * toDegrees)
}

fun List<PathCommand>.toAbsolute(): List<PathCommand> = trace().map { step ->
    when (val command = step.command) {
        is MoveTo -> MoveTo(step.end)
        is LineTo -> LineTo(step.end)
        is HorizontalTo -> HorizontalTo(step.end.x)
        is VerticalTo -> VerticalTo(step.end.y)
        is CubicTo -> CubicTo(step.controls[0], step.controls[1], step.end)
        is SmoothCubicTo -> SmoothCubicTo(step.controls[1], step.end)
        is QuadTo -> QuadTo(step.controls[0], step.end)
        is ArcTo -> command.copy(to = step.end, relative = false)
        Close -> Close
    }
}

fun List<PathCommand>.toRelative(): List<PathCommand> = trace().map { step ->
    val origin = step.start
    when (val command = step.command) {
        is MoveTo -> MoveTo(step.end - origin, relative = true)
        is LineTo -> LineTo(step.end - origin, relative = true)
        is HorizontalTo -> HorizontalTo(step.end.x - origin.x, relative = true)
        is VerticalTo -> VerticalTo(step.end.y - origin.y, relative = true)
        is CubicTo -> CubicTo(
            step.controls[0] - origin,
            step.controls[1] - origin,
            step.end - origin,
            relative = true,
        )

        is SmoothCubicTo -> SmoothCubicTo(step.controls[1] - origin, step.end - origin, relative = true)
        is QuadTo -> QuadTo(step.controls[0] - origin, step.end - origin, relative = true)
        is ArcTo -> command.copy(to = step.end - origin, relative = true)
        Close -> Close
    }
}

/**
 * Per-command shortest spelling — the demo version of vgo's CommandVariant(Compact).
 * Ties go to relative, like vgo, for output stability.
 */
fun List<PathCommand>.toCompact(precision: Int = 3): List<PathCommand> {
    val absolute = toAbsolute()
    val relative = toRelative()
    return absolute.zip(relative) { a, r ->
        if (a.toSvgText(precision).length < r.toSvgText(precision).length) a else r
    }
}
