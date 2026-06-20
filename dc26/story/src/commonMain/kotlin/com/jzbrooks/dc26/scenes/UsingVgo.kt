package com.jzbrooks.dc26.scenes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.template.Caption
import com.jzbrooks.dc26.template.GradientChip
import com.jzbrooks.dc26.template.SlideScaffold
import com.jzbrooks.dc26.template.TerminalPopup
import com.jzbrooks.dc26.theme.VgoColors
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import dev.bnorm.storyboard.toValue

fun StoryboardBuilder.UsingVgo() {
    scene(
        frameCount = 4,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        Box(Modifier.fillMaxSize()) {
            SlideScaffold {
                transition.AnimatedVisibility(
                    visible = { it.toValue() >= 3 },
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                        verticalArrangement = Arrangement.spacedBy(56.dp),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(32.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            GradientChip("Gradle")
                            Text(
                                text = "id(\"com.jzbrooks.vgo\")",
                                color = VgoColors.OnDark,
                                style = MaterialTheme.typography.body1,
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(32.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            GradientChip("IntelliJ")
                            Caption("available on JetBrains Marketplace")
                        }
                    }
                }
            }

            TerminalPopup(
                visible = { it.toValue() < 3 },
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

                transition.AnimatedVisibility(
                    visible = { it.toValue() >= 1 },
                    enter = expandVertically() + fadeIn(),
                    exit = fadeOut(),
                ) {
                    Column {
                        Spacer(Modifier.height(32.dp))
                        Text(
                            """
                            vgo -s *.xml

                            ic_home.xml
                            Size before: 4.12 KiB
                            Size after: 1.83 KiB
                            Percent saved: 55.6

                            ic_search.xml
                            Size before: 3.03 KiB
                            Size after: 1.46 KiB
                            Percent saved: 51.9
                            """.trimIndent()
                        )
                    }
                }

                transition.AnimatedVisibility(
                    visible = { it.toValue() >= 2 },
                    enter = expandVertically() + fadeIn(),
                    exit = fadeOut(),
                ) {
                    Column {
                        Spacer(Modifier.height(32.dp))
                        Text(
                            """
                            vgo --format iv icon.svg
                            """.trimIndent()
                        )
                    }
                }
            }
        }
    }
}
