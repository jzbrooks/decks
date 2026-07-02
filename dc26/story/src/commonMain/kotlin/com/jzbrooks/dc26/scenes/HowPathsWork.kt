package com.jzbrooks.dc26.scenes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.template.Caption
import com.jzbrooks.dc26.template.CodeTextStyle
import com.jzbrooks.dc26.template.SlideScaffold
import com.jzbrooks.dc26.theme.GradientText
import com.jzbrooks.dc26.theme.VgoColors
import com.jzbrooks.dc26.vector.ArcTo
import com.jzbrooks.dc26.vector.Close
import com.jzbrooks.dc26.vector.CubicTo
import com.jzbrooks.dc26.vector.HorizontalTo
import com.jzbrooks.dc26.vector.LineTo
import com.jzbrooks.dc26.vector.MoveTo
import com.jzbrooks.dc26.vector.PathCanvas
import com.jzbrooks.dc26.vector.PathCommand
import com.jzbrooks.dc26.vector.Point
import com.jzbrooks.dc26.vector.QuadTo
import com.jzbrooks.dc26.vector.SmoothCubicTo
import com.jzbrooks.dc26.vector.VerticalTo
import com.jzbrooks.dc26.vector.commandTokens
import com.jzbrooks.dc26.vector.dragHandles
import com.jzbrooks.dc26.vector.joined
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import dev.bnorm.storyboard.toValue

// A little sailboat: every command type gets a cameo.
private val BOAT: List<PathCommand> = listOf(
    MoveTo(Point(3f, 16f)),                                              // 0  hull: deck start
    HorizontalTo(21f),                                                   // 1  deck
    CubicTo(Point(19f, 20f), Point(16f, 21f), Point(12f, 21f)),          // 2  hull belly
    SmoothCubicTo(Point(5f, 19f), Point(3f, 16f)),                       // 3  hull belly, mirrored
    Close,                                                               // 4
    MoveTo(Point(12f, 13f)),                                             // 5  mast base
    VerticalTo(3f),                                                      // 6  mast
    LineTo(Point(18f, 13f)),                                             // 7  sail
    Close,                                                               // 8
    MoveTo(Point(5f, 6f)),                                               // 9  sun
    ArcTo(3f, 3f, 0f, largeArc = false, sweep = true, Point(11f, 6f)),   // 10 sun dome
    Close,                                                               // 11
    MoveTo(Point(2f, 23f)),                                              // 12 wave
    QuadTo(Point(7f, 21.5f), Point(12f, 23f)),                           // 13
    QuadTo(Point(17f, 24.5f), Point(22f, 23f)),                          // 14
)

private val VIEWPORT = Rect(0f, 0f, 24f, 26f)

private class PathFrame(
    val upTo: Int,
    val letter: String,
    val name: String,
    val detail: String,
    val freePlay: Boolean = false,
)

private val FRAMES = listOf(
    PathFrame(1, "M", "move to", "lift the pen and drop at new coordinates"),
    PathFrame(2, "H", "horizontal line", "draw to the specified x"),
    PathFrame(3, "C", "cubic Bézier", "two control points pull on the segment"),
    PathFrame(4, "S", "smooth cubic", "the previous control point is mirrored"),
    PathFrame(5, "Z", "close path", "a straight line back to the subpath start"),
    PathFrame(7, "M V", "a new subpath", "another M starts fresh;\nV is a vertical line"),
    PathFrame(9, "L Z", "line to", "a line to any point"),
    PathFrame(11, "A", "elliptical arc", "radii, a rotation, two flags, an endpoint"),
    PathFrame(15, "Q", "quadratic Bézier", "one control point, shared by both ends"),
    PathFrame(15, "✋", "free play", "drag any control point", freePlay = true),
)

// Which command's control points can be dragged, and which field each handle edits.
private class HandleRef(val command: Int, val slot: Int)

fun StoryboardBuilder.HowPathsWork() {
    scene(
        frames = FRAMES,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold {
            val frame = transition.currentState.toValue()
            var edited by remember { mutableStateOf(BOAT) }
            val commands = if (frame.freePlay) edited else BOAT

            val handleRefs = remember(commands) {
                commands.flatMapIndexed { index, command ->
                    when (command) {
                        is CubicTo -> listOf(HandleRef(index, 0), HandleRef(index, 1))
                        is SmoothCubicTo -> listOf(HandleRef(index, 1))
                        is QuadTo -> listOf(HandleRef(index, 0))
                        else -> emptyList()
                    }
                }
            }

            fun handlePoints(): List<Point> = handleRefs.map { ref ->
                when (val command = commands[ref.command]) {
                    is CubicTo -> if (ref.slot == 0) command.control1 else command.control2
                    is SmoothCubicTo -> command.control2
                    is QuadTo -> command.control
                    else -> error("handle on command without controls")
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(40.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(96.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val dragModifier = if (frame.freePlay) {
                        Modifier.dragHandles(VIEWPORT, ::handlePoints) { index, position ->
                            val ref = handleRefs[index]
                            edited = edited.mapIndexed { i, command ->
                                if (i != ref.command) command
                                else when (command) {
                                    is CubicTo ->
                                        if (ref.slot == 0) command.copy(control1 = position)
                                        else command.copy(control2 = position)

                                    is SmoothCubicTo -> command.copy(control2 = position)
                                    is QuadTo -> command.copy(control = position)
                                    else -> command
                                }
                            }
                        }
                    } else {
                        Modifier
                    }

                    PathCanvas(
                        commands = commands,
                        viewport = VIEWPORT,
                        progress = frame.upTo,
                        showPen = true,
                        showControlPoints = true,
                        controlFilter = {
                            if (frame.freePlay) {
                                true
                            } else {
                                it == frame.upTo - 1
                            }
                        },
                        fill = VgoColors.PathFill,
                        modifier = Modifier
                            .size(600.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .background(VgoColors.Surface)
                            .then(dragModifier),
                    )

                    Column(
                        Modifier.width(680.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                    ) {
                        GradientText(frame.letter, style = MaterialTheme.typography.h1)
                        Text(frame.name, style = MaterialTheme.typography.h3)
                        Caption(frame.detail)
                    }
                }

                ProvideTextStyle(CodeTextStyle) {
                    Text(
                        commands.commandTokens(highlightIndex = frame.upTo - 1).joined(),
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    )
                }
            }
        }
    }
}
