package com.jzbrooks.dc26.scenes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jzbrooks.dc26.template.Caption
import com.jzbrooks.dc26.template.GradientChip
import com.jzbrooks.dc26.template.SlideScaffold
import com.jzbrooks.dc26.template.Terminal
import com.jzbrooks.dc26.theme.GradientText
import com.jzbrooks.dc26.theme.VgoColors
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import dev.bnorm.storyboard.toValue

fun StoryboardBuilder.WhatIsVgo() {
    scene(
        frameCount = 3,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold {
            val frame = transition.currentState.toValue()

            BoxWithConstraints(Modifier.fillMaxSize()) {
                // The terminal starts peeking in from the bottom of the scene
                // (lower corners just off-screen), then the whole column slides
                // up and locks with the terminal at the top of the body.
                var terminalHeightPx by remember { mutableIntStateOf(0) }
                val overhangPx = with(LocalDensity.current) { 84.dp.roundToPx() }
                val peekOffsetPx = constraints.maxHeight - terminalHeightPx + overhangPx
                val offsetY by animateIntAsState(
                    targetValue = if (frame >= 1) 0 else peekOffsetPx,
                    animationSpec = tween(600, easing = EaseInOutCubic),
                )

                Column(
                    Modifier.offset { IntOffset(0, offsetY) },
                    verticalArrangement = Arrangement.spacedBy(40.dp),
                ) {
                    Terminal(
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                            .align(Alignment.CenterHorizontally)
                            .onSizeChanged { terminalHeightPx = it.height },
                        title = "icons — zsh",
                    ) {
                        ProvideTextStyle(LocalTextStyle.current.copy(fontSize = 26.sp, lineHeight = 40.sp)) {
                            Text(
                                buildAnnotatedString {
                                    withStyle(SpanStyle(color = VgoColors.Muted)) { append("% ") }
                                    append(
                                        """
                                        vgo -s icon.svg
                                        Size before: 2.80 KiB
                                        Size after: 1.02 KiB
                                        Percent saved: 63.6
                                        """.trimIndent()
                                    )
                                }
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            Text("parse", style = MaterialTheme.typography.h4)
                            Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                                GradientChip("SVG")
                                GradientChip("VectorDrawable")
                                GradientChip("ImageVector")
                            }
                        }
                        Text("→", style = MaterialTheme.typography.h4, color = VgoColors.Muted)
                        GradientText("optimize the IR", style = MaterialTheme.typography.h4)
                        Text("→", style = MaterialTheme.typography.h4, color = VgoColors.Muted)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            Text("write", style = MaterialTheme.typography.h4)
                            Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                                GradientChip("SVG")
                                GradientChip("VectorDrawable")
                                GradientChip("ImageVector")
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible = frame >= 2,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                        exit = fadeOut(),
                    ) {
                        Column {
                            GradientText("Up to 65% smaller", style = MaterialTheme.typography.h2)
                            Caption("on real-world artwork")
                        }
                    }
                }
            }
        }
    }
}
