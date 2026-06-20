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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path as DrawPath
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.template.Caption
import com.jzbrooks.dc26.template.SlideScaffold
import com.jzbrooks.dc26.theme.VgoColors
import com.jzbrooks.dc26.vector.Close
import com.jzbrooks.dc26.vector.LineTo
import com.jzbrooks.dc26.vector.MoveTo
import com.jzbrooks.dc26.vector.PathCanvas
import com.jzbrooks.dc26.vector.PathCommand
import com.jzbrooks.dc26.vector.Point
import com.jzbrooks.dc26.vector.drawCommands
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.RevealEach
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import dev.bnorm.storyboard.toValue

// Two triangles in opposite corners: bounding boxes overlap but shapes do not.
// Shape A (amber) occupies upper-left; shape B (azure) occupies lower-right.
// A lies entirely in the region x+y ≤ 22; B lies entirely in x+y ≥ 32.
private val SHAPE_A: List<PathCommand> = listOf(
    MoveTo(Point(2f, 2f)),
    LineTo(Point(20f, 2f)),
    LineTo(Point(2f, 20f)),
    Close,
)

private val SHAPE_B: List<PathCommand> = listOf(
    MoveTo(Point(10f, 22f)),
    LineTo(Point(22f, 22f)),
    LineTo(Point(22f, 10f)),
    Close,
)

private val VIEWPORT = Rect(0f, 0f, 24f, 24f)

// AABB of A: (2,2)–(20,20)   AABB of B: (10,10)–(22,22)   Overlap: (10,10)–(20,20)
private val A_TL = Point(2f, 2f);  private val A_BR = Point(20f, 20f)
private val B_TL = Point(10f, 10f); private val B_BR = Point(22f, 22f)
private val OV_TL = Point(10f, 10f); private val OV_BR = Point(20f, 20f)

private val HULL_A = listOf(Point(2f, 2f), Point(20f, 2f), Point(2f, 20f))
private val HULL_B = listOf(Point(10f, 22f), Point(22f, 22f), Point(22f, 10f))

fun StoryboardBuilder.MergePathsAlgorithm() {
    scene(
        frameCount = 3,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold {
            val frame = transition.currentState.toValue()

            Row(
                horizontalArrangement = Arrangement.spacedBy(96.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PathCanvas(
                    commands = SHAPE_A,
                    viewport = VIEWPORT,
                    fill = VgoColors.PathFill,
                    stroke = VgoColors.Amber,
                    modifier = Modifier
                        .size(680.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(VgoColors.Surface),
                ) { transform ->
                    drawCommands(transform, SHAPE_B, VgoColors.Azure, fill = VgoColors.PathFill)

                    // Frame 1+: AABB outlines and highlighted overlap region
                    if (frame >= 1) {
                        val dash = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)

                        val aTL = transform.toScreen(A_TL)
                        val aBR = transform.toScreen(A_BR)
                        drawRect(VgoColors.Amber, aTL, Size(aBR.x - aTL.x, aBR.y - aTL.y), style = Stroke(4.dp.toPx(), pathEffect = dash))

                        val bTL = transform.toScreen(B_TL)
                        val bBR = transform.toScreen(B_BR)
                        drawRect(VgoColors.Azure, bTL, Size(bBR.x - bTL.x, bBR.y - bTL.y), style = Stroke(4.dp.toPx(), pathEffect = dash))

                        val ovTL = transform.toScreen(OV_TL)
                        val ovBR = transform.toScreen(OV_BR)
                        drawRect(VgoColors.Muted.copy(alpha = 0.25f), ovTL, Size(ovBR.x - ovTL.x, ovBR.y - ovTL.y))
                    }

                    // Frame 2+: convex hull outlines to illustrate what GJK operates on
                    if (frame >= 2) {
                        val dashes = PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f)
                        for (hull in listOf(HULL_A, HULL_B)) {
                            val path = DrawPath().apply {
                                hull.forEachIndexed { i, p ->
                                    val sp = transform.toScreen(p)
                                    if (i == 0) moveTo(sp.x, sp.y) else lineTo(sp.x, sp.y)
                                }
                                close()
                            }
                            drawPath(path, VgoColors.Magenta, style = Stroke(6.dp.toPx(), pathEffect = dashes))
                        }
                    }
                }

                Column(
                    Modifier.width(840.dp).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                ) {
                    Text("how it works", style = MaterialTheme.typography.h4)

                    RevealEach(transition.createChildTransition { it.toValue() }) {
                        item(0) {
                            AlgoStep(
                                label = "1. bounding-box check",
                                detail = "Compare axis-aligned extents — O(1). " +
                                    "Disjoint boxes guarantee no intersection. " +
                                    "Here the boxes overlap, so this fast check is inconclusive.",
                            )
                        }
                        item(1) {
                            AlgoStep(
                                label = "2. overlap isn't intersection",
                                detail = "The shapes occupy opposite corners of their shared bounding box. " +
                                    "AABB overlap is a necessary condition for intersection — not sufficient.",
                            )
                        }
                        item(2) {
                            AlgoStep(
                                label = "3. GJK on convex hulls",
                                detail = "Tests whether the hulls actually intersect. More expensive, " +
                                    "but only runs when the fast check fails. " +
                                    "Confirms these paths are disjoint — safe to merge.",
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AlgoStep(label: String, detail: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = MaterialTheme.typography.subtitle1, color = VgoColors.Azure)
        Caption(detail)
    }
}
