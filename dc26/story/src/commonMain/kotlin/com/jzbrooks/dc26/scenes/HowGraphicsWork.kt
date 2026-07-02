package com.jzbrooks.dc26.scenes

import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.template.Caption
import com.jzbrooks.dc26.template.CodeTextStyle
import com.jzbrooks.dc26.template.SlideScaffold
import com.jzbrooks.dc26.theme.VgoColors
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.RevealEach
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import dev.bnorm.storyboard.toValue
import kotlin.math.sqrt

fun StoryboardBuilder.HowGraphicsWork() {
    scene(
        frameCount = 3,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold {
            var zoom by remember { mutableFloatStateOf(1f) }

            Row(
                horizontalArrangement = Arrangement.spacedBy(96.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(64.dp)) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            RasterCircle(zoom)
                            Text("raster — samples", color = VgoColors.Amber)
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            VectorCircle(zoom)
                            Text("vector — instructions", color = VgoColors.Azure)
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("zoom", color = VgoColors.Muted)
                        Slider(
                            value = zoom,
                            onValueChange = { zoom = it },
                            valueRange = 1f..6f,
                            modifier = Modifier.width(560.dp),
                        )
                        Text("${(zoom * 10).toInt() / 10f}×", color = VgoColors.Muted)
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(48.dp),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    RevealEach(transition.createChildTransition { it.toValue() }) {
                        item(
                            index = 1,
                            enterTransition = { fadeIn() + expandVertically() },
                            exitTransition = { fadeOut() + shrinkVertically() },
                        ) {
                            Caption("rasters store samples;\nvectors store instructions")
                        }
                        item(
                            index = 2,
                            enterTransition = { fadeIn() + expandVertically() },
                            exitTransition = { fadeOut() + shrinkVertically() },
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                Caption("…and \"instructions\" are a scene graph:")
                                ProvideTextStyle(CodeTextStyle) {
                                    Text(
                                        """
                                        <svg>
                                         └─ <g transform="…">
                                             ├─ <path d="…"/>
                                             └─ <path d="…"/>
                                        """.trimIndent(),
                                        color = VgoColors.OnDark,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private const val SAMPLES = 256
private const val RADIUS_CELLS = 99.2f
private const val MAX_ZOOM = 6f

@androidx.compose.runtime.Composable
private fun RasterCircle(zoom: Float) {
    Canvas(
        Modifier
            .size(480.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(VgoColors.Surface)
    ) {
        val cell = size.width / SAMPLES * zoom
        val t = (zoom - 1f) / (MAX_ZOOM - 1f)
        val diag = RADIUS_CELLS / sqrt(2f)
        val origin = Offset(
            size.width / 2f - (SAMPLES / 2f + diag * t) * cell,
            size.height / 2f - (SAMPLES / 2f - diag * t) * cell,
        )
        for (row in 0 until SAMPLES) {
            for (column in 0 until SAMPLES) {
                val dx = column + 0.5f - SAMPLES / 2f
                val dy = row + 0.5f - SAMPLES / 2f
                val inside = sqrt(dx * dx + dy * dy) <= RADIUS_CELLS
                if (!inside) continue
                drawRect(
                    VgoColors.Amber,
                    topLeft = Offset(origin.x + column * cell, origin.y + row * cell),
                    size = androidx.compose.ui.geometry.Size(cell, cell),
                )
            }
        }

        val gridStroke = cell * 0.005f
        for (i in 0..SAMPLES) {
            val x = origin.x + i * cell
            drawLine(
                VgoColors.Background,
                start = Offset(x, origin.y),
                end = Offset(x, origin.y + SAMPLES * cell),
                strokeWidth = gridStroke,
            )
            val y = origin.y + i * cell
            drawLine(
                VgoColors.Background,
                start = Offset(origin.x, y),
                end = Offset(origin.x + SAMPLES * cell, y),
                strokeWidth = gridStroke,
            )
        }
    }
}

@androidx.compose.runtime.Composable
private fun VectorCircle(zoom: Float) {
    Canvas(
        Modifier
            .size(480.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(VgoColors.Surface)
    ) {
        val cell = size.width / SAMPLES * zoom
        val t = (zoom - 1f) / (MAX_ZOOM - 1f)
        val diag = RADIUS_CELLS / sqrt(2f)
        val radius = RADIUS_CELLS * cell
        val circleCenter = Offset(
            size.width / 2f - diag * t * cell,
            size.height / 2f + diag * t * cell,
        )
        drawCircle(VgoColors.PathFill, radius, circleCenter)
        drawCircle(VgoColors.Azure, radius, circleCenter, style = Stroke(6.dp.toPx()))
    }
}
