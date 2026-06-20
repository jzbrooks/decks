package com.jzbrooks.dc26.scenes

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.template.ByteChip
import com.jzbrooks.dc26.template.Caption
import com.jzbrooks.dc26.template.CodeTextStyle
import com.jzbrooks.dc26.template.SlideScaffold
import com.jzbrooks.dc26.theme.DC26_XML
import com.jzbrooks.dc26.theme.VgoColors
import com.jzbrooks.dc26.vector.Close
import com.jzbrooks.dc26.vector.HorizontalTo
import com.jzbrooks.dc26.vector.LineTo
import com.jzbrooks.dc26.vector.MoveTo
import com.jzbrooks.dc26.vector.PathCanvas
import com.jzbrooks.dc26.vector.PathCommand
import com.jzbrooks.dc26.vector.Point
import com.jzbrooks.dc26.vector.VerticalTo
import com.jzbrooks.dc26.vector.drawCommands
import com.jzbrooks.dc26.vector.toComposePath
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import dev.bnorm.storyboard.text.highlight.style
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.toValue

private val TRIANGLE: List<PathCommand> = listOf(
    MoveTo(Point(4f, 20f)),
    LineTo(Point(11f, 5f)),
    LineTo(Point(18f, 20f)),
    Close,
)

private val SQUARE: List<PathCommand> = listOf(
    MoveTo(Point(28f, 6f)),
    HorizontalTo(42f),
    VerticalTo(20f),
    HorizontalTo(28f),
    Close,
)

private val INTRUDER: List<PathCommand> = listOf(
    MoveTo(Point(14f, 10f)),
    HorizontalTo(30f),
    VerticalTo(16f),
    HorizontalTo(14f),
    Close,
)

private const val TRIANGLE_XML = """<path android:pathData="M4,20L11,5 18,20Z"/>"""
private const val SQUARE_XML = """<path android:pathData="M28,6h14v14h-14z"/>"""
private const val MERGED_XML = """<path android:pathData="M4,20L11,5 18,20ZM28,6h14v14h-14z"/>"""

private val VIEWPORT = Rect(0f, 0f, 48f, 24f)

fun StoryboardBuilder.MergePaths() {
    scene(
        frameCount = 4,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold {
            val frame = transition.currentState.toValue()

            val triangleBounds = remember { TRIANGLE.toComposePath().getBounds() }
            val squareBounds = remember { SQUARE.toComposePath().getBounds() }
            val intruderBounds = remember { INTRUDER.toComposePath().getBounds() }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(48.dp),
            ) {
                PathCanvas(
                    commands = TRIANGLE,
                    viewport = VIEWPORT,
                    fill = VgoColors.PathFill,
                    modifier = Modifier
                        .width(1240.dp)
                        .height(620.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(VgoColors.Surface),
                ) { transform ->
                    drawCommands(transform, SQUARE, VgoColors.Azure, fill = VgoColors.PathFill)
                    if (frame >= 3) {
                        drawCommands(transform, INTRUDER, VgoColors.Amber, fill = VgoColors.Amber.copy(alpha = 0.18f))
                    }

                    if (frame >= 1) {
                        val dash = PathEffect.dashPathEffect(floatArrayOf(12f, 10f))
                        val boxes = buildList {
                            add(triangleBounds to VgoColors.Handle)
                            add(squareBounds to VgoColors.Handle)
                            if (frame >= 3) add(intruderBounds to VgoColors.Amber)
                        }
                        for ((bounds, color) in boxes) {
                            val topLeft = transform.toScreen(Point(bounds.left, bounds.top))
                            val bottomRight = transform.toScreen(Point(bounds.right, bounds.bottom))
                            drawRect(
                                color,
                                topLeft = topLeft,
                                size = androidx.compose.ui.geometry.Size(
                                    bottomRight.x - topLeft.x,
                                    bottomRight.y - topLeft.y,
                                ),
                                style = Stroke(4.dp.toPx(), pathEffect = dash),
                            )
                        }
                    }
                }

                ProvideTextStyle(CodeTextStyle) {
                    val xml: Transition<AnnotatedString> = transition.createChildTransition {
                        if (it.toValue() >= 2) MERGED_XML.style(DC26_XML)
                        else buildAnnotatedString {
                            append(TRIANGLE_XML.style(DC26_XML))
                            append("\n")
                            append(SQUARE_XML.style(DC26_XML))
                        }
                    }
                    MagicText(xml)
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    when {
                        frame >= 3 -> Caption("overlapping bounds — paint order could matter, so vgo keeps them apart")
                        frame >= 2 -> {
                            ByteChip(TRIANGLE_XML.length + SQUARE_XML.length + 1, color = VgoColors.Amber)
                            Caption("→")
                            ByteChip(MERGED_XML.length)
                            Caption("same paint + disjoint bounds = one path, two subpaths")
                        }

                        frame >= 1 -> Caption("the bounding boxes don't intersect…")
                        else -> Caption("two paths, identical paint")
                    }
                }
            }
        }
    }
}
