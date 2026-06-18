package com.jzbrooks.dc26.scenes

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.template.SlideScaffold
import com.jzbrooks.dc26.theme.VgoColors
import com.jzbrooks.dc26.theme.VgoGradient
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import dev.bnorm.storyboard.toValue

private data class TimelineEntry(val date: String, val event: String, val textAbove: Boolean)

private val ENTRIES = listOf(
    TimelineEntry("2018", "building an app — PDF icons on iOS, VectorDrawable on Android", true),
    TimelineEntry("August 2019", "initial commit — VectorDrawable & PDF targets", false),
    TimelineEntry("September 2019", "iOS 13 ships", true),
    TimelineEntry("October 2019", "SVG support lands", false),
    TimelineEntry("July 2021", "Compose 1.0 — ImageVector API ships", true),
    TimelineEntry("May 2025", "ImageVector support lands in vgo", false),
)

fun StoryboardBuilder.VgoHistory() {
    scene(
        frameCount = 6,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .align(Alignment.Center),
            ) {
                drawLine(
                    brush = VgoGradient,
                    start = Offset(0f, size.height / 2f),
                    end = Offset(size.width, size.height / 2f),
                    strokeWidth = size.height,
                )
            }

            val revealTransition = transition.createChildTransition { it.toValue() }

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ENTRIES.forEachIndexed { index, entry ->
                    val alpha by revealTransition.animateFloat(
                        transitionSpec = { tween(durationMillis = 400) },
                        label = "node_${index}_alpha",
                    ) { frame -> if (frame >= index) 1f else 0f }

                    TimelineNode(
                        date = entry.date,
                        event = entry.event,
                        textAbove = entry.textAbove,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .alpha(alpha),
                    )
                }
            }
        }
    }
}

@Composable
private fun TimelineNode(date: String, event: String, textAbove: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (textAbove) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter,
            ) {
                NodeLabel(date, event, Modifier.padding(bottom = 20.dp))
            }
        } else {
            Spacer(Modifier.weight(1f))
        }

        Canvas(Modifier.size(14.dp)) {
            drawCircle(brush = VgoGradient, radius = size.width / 2f)
        }

        if (!textAbove) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.TopCenter,
            ) {
                NodeLabel(date, event, Modifier.padding(top = 20.dp))
            }
        } else {
            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun NodeLabel(date: String, event: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = date,
            color = VgoColors.Muted,
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Center,
        )
        Text(
            text = event,
            color = VgoColors.OnDark,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
        )
    }
}
