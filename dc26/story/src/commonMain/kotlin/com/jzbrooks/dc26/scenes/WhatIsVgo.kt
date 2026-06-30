package com.jzbrooks.dc26.scenes

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.template.Caption
import com.jzbrooks.dc26.template.GradientChip
import com.jzbrooks.dc26.template.SlideScaffold
import com.jzbrooks.dc26.template.TerminalPopup
import com.jzbrooks.dc26.theme.GradientText
import com.jzbrooks.dc26.theme.VgoColors
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.RevealEach
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import dev.bnorm.storyboard.toValue

fun StoryboardBuilder.WhatIsVgo() {
    scene(
        frameCount = 3,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        Box(Modifier.fillMaxSize()) {
            SlideScaffold {
                Column(
                    Modifier.padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(64.dp),
                ) {
                    RevealEach(transition.createChildTransition { it.toValue() }) {
                        item {
                            Column {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    ) {
                                        Text("parse", style = MaterialTheme.typography.h4)
                                        Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
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
                                        Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                                            GradientChip("SVG")
                                            GradientChip("VectorDrawable")
                                            GradientChip("ImageVector")
                                        }
                                    }
                                }
                            }
                        }

                        item(index = 2) {
                            Column {
                                GradientText("Up to 65% smaller", style = MaterialTheme.typography.h1)
                                Caption("on real-world artwork")
                            }
                        }
                    }
                }
            }

            TerminalPopup(
                visible = { it.toValue() == 1 },
                title = "icons — zsh",
            ) {
                Text(
                    """
                    vgo -s icon.svg
                    Size before: 2.80 KiB
                    Size after: 1.02 KiB
                    Percent saved: 63.6
                    """.trimIndent()
                )
            }
        }
    }
}
