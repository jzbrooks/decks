package com.jzbrooks.dc26.vector

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import kotlin.math.abs
import kotlin.math.round

/**
 * Format a coordinate the way vgo's writers do: fixed precision,
 * trailing zeros trimmed, no `String.format` (wasm-safe).
 */
fun Float.toCoordinateString(precision: Int = 3): String {
    var factor = 1L
    repeat(precision) { factor *= 10 }
    val scaled = round(this.toDouble() * factor).toLong()
    if (scaled == 0L) return "0"

    val digits = abs(scaled).toString().padStart(precision + 1, '0')
    val whole = digits.dropLast(precision)
    val fraction = digits.takeLast(precision).trimEnd('0')

    return buildString {
        if (scaled < 0) append('-')
        append(whole)
        if (fraction.isNotEmpty()) {
            append('.')
            append(fraction)
        }
    }
}

private fun PathCommand.letter(): Char {
    val upper =
        when (this) {
            is MoveTo -> 'M'
            is LineTo -> 'L'
            is HorizontalTo -> 'H'
            is VerticalTo -> 'V'
            is CubicTo -> 'C'
            is SmoothCubicTo -> 'S'
            is QuadTo -> 'Q'
            is ArcTo -> 'A'
            Close -> 'Z'
        }
    return if (relative) upper.lowercaseChar() else upper
}

private fun PathCommand.parameterStrings(precision: Int): List<String> =
    when (this) {
        is MoveTo -> {
            listOf(to.x.toCoordinateString(precision), to.y.toCoordinateString(precision))
        }

        is LineTo -> {
            listOf(to.x.toCoordinateString(precision), to.y.toCoordinateString(precision))
        }

        is HorizontalTo -> {
            listOf(x.toCoordinateString(precision))
        }

        is VerticalTo -> {
            listOf(y.toCoordinateString(precision))
        }

        is CubicTo -> {
            listOf(control1.x, control1.y, control2.x, control2.y, to.x, to.y)
                .map { it.toCoordinateString(precision) }
        }

        is SmoothCubicTo -> {
            listOf(control2.x, control2.y, to.x, to.y)
                .map { it.toCoordinateString(precision) }
        }

        is QuadTo -> {
            listOf(control.x, control.y, to.x, to.y)
                .map { it.toCoordinateString(precision) }
        }

        is ArcTo -> {
            listOf(
                rx.toCoordinateString(precision),
                ry.toCoordinateString(precision),
                rotation.toCoordinateString(precision),
                if (largeArc) "1" else "0",
                if (sweep) "1" else "0",
                to.x.toCoordinateString(precision),
                to.y.toCoordinateString(precision),
            )
        }

        Close -> {
            emptyList()
        }
    }

/**
 * Render a command the way vgo writes one: comma separators,
 * elided before negative numbers (the `-` is separator enough).
 */
fun PathCommand.toSvgText(precision: Int = 3): String =
    buildString {
        append(letter())
        for ((index, parameter) in parameterStrings(precision).withIndex()) {
            if (index > 0 && !parameter.startsWith('-')) append(',')
            append(parameter)
        }
    }

fun List<PathCommand>.toPathString(precision: Int = 3): String = joinToString(separator = "") { it.toSvgText(precision) }

fun List<PathCommand>.byteCount(precision: Int = 3): Int = toPathString(precision).encodeToByteArray().size

/**
 * One AnnotatedString per command — stable token boundaries keep
 * MagicText morphs anchored to commands rather than characters.
 */
fun List<PathCommand>.toTokens(
    letterStyle: SpanStyle,
    numberStyle: SpanStyle,
    precision: Int = 3,
    highlightIndex: Int = -1,
    highlightStyle: SpanStyle = SpanStyle(),
): List<AnnotatedString> =
    mapIndexed { index, command ->
        val text = command.toSvgText(precision)
        val highlight = if (index == highlightIndex) highlightStyle else SpanStyle()
        buildAnnotatedString {
            withStyle(letterStyle.merge(highlight)) { append(text.first()) }
            if (text.length > 1) {
                withStyle(numberStyle.merge(highlight)) { append(text.substring(1)) }
            }
        }
    }

fun List<AnnotatedString>.joined(): AnnotatedString =
    buildAnnotatedString {
        for (token in this@joined) append(token)
    }
