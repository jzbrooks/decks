package com.jzbrooks.dc26.template

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.theme.GradientText
import com.jzbrooks.dc26.theme.VgoColors
import com.jzbrooks.dc26.theme.VgoGradient
import dev.bnorm.storyboard.StoryboardBuilder

fun StoryboardBuilder.TodoStubScene(title: String, vararg notes: String) {
    scene(frameCount = 1) {
        TodoStub(title, notes.toList())
    }
}

@Composable
fun TodoStub(title: String, notes: List<String>) {
    Box(Modifier.fillMaxSize().padding(48.dp)) {
        Box(
            Modifier
                .fillMaxSize()
                .drawBehind {
                    drawRoundRect(
                        brush = VgoGradient,
                        cornerRadius = CornerRadius(24.dp.toPx()),
                        style = Stroke(
                            width = 5.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(28f, 18f)),
                        ),
                    )
                }
        ) {
            Column(
                Modifier.align(Alignment.Center).padding(64.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                GradientText(title, style = MaterialTheme.typography.h2, textAlign = TextAlign.Center)
                for (note in notes) {
                    Text(
                        note,
                        color = VgoColors.Muted,
                        style = MaterialTheme.typography.h4,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(36.dp)
                    .rotate(8f)
                    .background(VgoColors.Amber, RoundedCornerShape(10.dp))
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    "TODO",
                    color = VgoColors.Background,
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Black,
                )
            }
        }
    }
}
