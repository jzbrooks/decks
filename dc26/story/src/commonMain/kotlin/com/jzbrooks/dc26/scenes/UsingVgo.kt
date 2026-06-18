package com.jzbrooks.dc26.scenes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
        SlideScaffold {
            transition.AnimatedVisibility(
                visible = { it.toValue() >= 3 },
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(28.dp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
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
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        GradientChip("IntelliJ")
                        Caption("available on JetBrains Marketplace")
                    }
                }
            }

            TerminalPopup(
                visible = { it.toValue() < 3 },
                title = "icons — zsh",
            ) {
                Text(
                    """
                    $ vgo --stats icon.svg
                    icon.svg
                    2,867 → 1,043 bytes (-63.6%)
                    """.trimIndent()
                )

                transition.AnimatedVisibility(
                    visible = { it.toValue() >= 1 },
                    enter = expandVertically() + fadeIn(),
                    exit = fadeOut(),
                ) {
                    Column {
                        Spacer(Modifier.height(16.dp))
                        Text(
                            """
                            $ vgo --stats res/drawable/*.xml
                            ic_home.xml: 4,221 → 1,876 bytes (-55.6%)
                            ic_search.xml: 3,104 → 1,492 bytes (-51.9%)
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
                        Spacer(Modifier.height(16.dp))
                        // --format flag syntax is inferred; verify against vgo's actual CLI
                        Text(
                            """
                            $ vgo --format imagevector icon.svg
                            icon.kt written
                            """.trimIndent()
                        )
                    }
                }
            }
        }
    }
}
