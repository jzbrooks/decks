package com.jzbrooks.dc26.scenes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.template.Caption
import com.jzbrooks.dc26.template.TerminalPopup
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
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import dev.bnorm.storyboard.toValue

// The same sailboat from the path-commands scene, sailing again in a terminal.
private val BOAT: List<PathCommand> = listOf(
    MoveTo(Point(3f, 16f)),
    HorizontalTo(21f),
    CubicTo(Point(19f, 20f), Point(16f, 21f), Point(12f, 21f)),
    SmoothCubicTo(Point(5f, 19f), Point(3f, 16f)),
    Close,
    MoveTo(Point(12f, 13f)),
    VerticalTo(3f),
    LineTo(Point(18f, 13f)),
    Close,
    MoveTo(Point(5f, 6f)),
    ArcTo(3f, 3f, 0f, largeArc = false, sweep = true, Point(11f, 6f)),
    Close,
    MoveTo(Point(2f, 23f)),
    QuadTo(Point(7f, 21.5f), Point(12f, 23f)),
    QuadTo(Point(17f, 24.5f), Point(22f, 23f)),
)

fun StoryboardBuilder.Vat() {
    scene(
        frameCount = 2,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        Box(Modifier.fillMaxSize()) {
            Caption(
                "vat — vector art in your terminal (kitty graphics protocol)",
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 96.dp),
            )

            TerminalPopup(
                visible = { it != Frame.End },
                title = "boats — zsh",
            ) {
                Text("$ vat boat.svg")
                transition.AnimatedVisibility(
                    visible = { it.toValue() >= 1 },
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut(),
                ) {
                    PathCanvas(
                        commands = BOAT,
                        viewport = Rect(0f, 0f, 24f, 26f),
                        showGrid = false,
                        fill = VgoColors.PathFill,
                        modifier = Modifier.size(480.dp).padding(top = 16.dp),
                    )
                }
            }
        }
    }
}
