package com.jzbrooks.dc26.vector

data class Point(
    val x: Float,
    val y: Float,
) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)

    operator fun minus(other: Point) = Point(x - other.x, y - other.y)
}

// A deck-sized model of SVG path commands. vgo-core has the real one; this only
// needs to be rich enough to draw, rewrite, and measure demo paths on slides.
sealed interface PathCommand {
    val relative: Boolean
}

data class MoveTo(
    val to: Point,
    override val relative: Boolean = false,
) : PathCommand

data class LineTo(
    val to: Point,
    override val relative: Boolean = false,
) : PathCommand

data class HorizontalTo(
    val x: Float,
    override val relative: Boolean = false,
) : PathCommand

data class VerticalTo(
    val y: Float,
    override val relative: Boolean = false,
) : PathCommand

data class CubicTo(
    val control1: Point,
    val control2: Point,
    val to: Point,
    override val relative: Boolean = false,
) : PathCommand

data class SmoothCubicTo(
    val control2: Point,
    val to: Point,
    override val relative: Boolean = false,
) : PathCommand

data class QuadTo(
    val control: Point,
    val to: Point,
    override val relative: Boolean = false,
) : PathCommand

data class ArcTo(
    val rx: Float,
    val ry: Float,
    val rotation: Float,
    val largeArc: Boolean,
    val sweep: Boolean,
    val to: Point,
    override val relative: Boolean = false,
) : PathCommand

data object Close : PathCommand {
    override val relative: Boolean get() = false
}
