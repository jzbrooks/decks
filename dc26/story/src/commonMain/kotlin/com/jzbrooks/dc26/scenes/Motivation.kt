package com.jzbrooks.dc26.scenes

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jzbrooks.dc26.template.Caption
import com.jzbrooks.dc26.template.GradientChip
import com.jzbrooks.dc26.template.SlideScaffold
import com.jzbrooks.dc26.template.Terminal
import com.jzbrooks.dc26.theme.GradientText
import com.jzbrooks.dc26.theme.VgoColors
import com.jzbrooks.dc26.theme.VgoGradient
import com.jzbrooks.deck.story.generated.resources.Res
import com.jzbrooks.deck.story.generated.resources.ic_add_a_profile_megaphone_image
import com.jzbrooks.deck.story.generated.resources.ic_notification_permission
import com.jzbrooks.deck.story.generated.resources.ic_onboarding
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.RevealEach
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import dev.bnorm.storyboard.toValue
import org.jetbrains.compose.resources.painterResource

@Composable
private fun ScreenshotPlaceholder(label: String, modifier: Modifier = Modifier) {
    Box(
        modifier.drawBehind {
            drawRoundRect(
                brush = VgoGradient,
                cornerRadius = CornerRadius(24.dp.toPx()),
                style = Stroke(
                    width = 4.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 12f)),
                ),
            )
        },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("[ screenshot ]", color = VgoColors.Muted, style = MaterialTheme.typography.body1)
            Text(label, color = VgoColors.Muted, style = MaterialTheme.typography.body2, textAlign = TextAlign.Center)
        }
    }
}

fun StoryboardBuilder.MotivationTooling() {
    scene(
        frameCount = 2,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(64.dp),
            ) {
                RevealEach(transition.createChildTransition { it.toValue() }) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(40.dp)) {
                            GradientText(
                                "Android tooling is great.",
                                style = MaterialTheme.typography.h3,
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                                GradientChip("Android Studio")
                                GradientChip("Gradle")
                                GradientChip("Lint")
                                GradientChip("ADB")
                            }
                        }
                    }
                    item {
                        Text(
                            "So workflow pain points stand out.",
                            style = MaterialTheme.typography.h3,
                            color = VgoColors.OnDark,
                        )
                    }
                }
            }
        }
    }
}

fun StoryboardBuilder.MotivationVectorPain() {
    scene(
        frameCount = 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold {
            Row(
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(64.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                ) {
                    Text(
                        "Individual illustrations were getting big.",
                        style = MaterialTheme.typography.h3,
                        color = VgoColors.OnDark,
                    )
                    Text(
                        """
                            |VectorDrawable on Android
                            |PDF on iOS
                        """.trimMargin(),
                        style = MaterialTheme.typography.body1,
                        color = VgoColors.Muted,
                    )
                }
                Box(
                    Modifier.weight(1f).fillMaxHeight(),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .offset(x = (-260).dp, y = (-160).dp)
                            .rotate(-5f),
                    ) {
                        Image(
                            painterResource(Res.drawable.ic_notification_permission),
                            contentDescription = null,
                            modifier = Modifier.size(380.dp),
                        )
                        Text("Firefox", color = VgoColors.OnDark, style = MaterialTheme.typography.body1, fontWeight = FontWeight.SemiBold)
                        Text("126 KB", color = VgoColors.Muted, style = MaterialTheme.typography.body2)
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .offset(x = 170.dp, y = (-290).dp)
                            .rotate(6f),
                    ) {
                        Image(
                            painterResource(Res.drawable.ic_onboarding),
                            contentDescription = null,
                            modifier = Modifier.size(380.dp),
                        )
                        Text("Rocket.Chat", color = VgoColors.OnDark, style = MaterialTheme.typography.body1, fontWeight = FontWeight.SemiBold)
                        Text("36 KB", color = VgoColors.Muted, style = MaterialTheme.typography.body2)
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .offset(x = 0.dp, y = 150.dp)
                            .rotate(-3f),
                    ) {
                        Image(
                            painterResource(Res.drawable.ic_add_a_profile_megaphone_image),
                            contentDescription = null,
                            modifier = Modifier.size(380.dp),
                        )
                        Text("Signal", color = VgoColors.OnDark, style = MaterialTheme.typography.body1, fontWeight = FontWeight.SemiBold)
                        Text("43 KB", color = VgoColors.Muted, style = MaterialTheme.typography.body2)
                    }
                }
            }
        }
    }
}

fun StoryboardBuilder.MotivationNode() {
    scene(
        frameCount = 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold {
            Row(
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(64.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Terminal(
                    modifier = Modifier.weight(1f),
                    title = "nowinandroid — zsh",
                ) {
                    Text("> git log -1 377d3be", fontSize = 22.sp)
                    Text(
                        """
                        commit 377d3bef0cef20984dfab154bc2ad0c597b7c0b8
                        Author: Simon Marquis <contact@simon-marquis.fr>
                        Date:   Fri Jun  2 21:43:25 2023 +0000

                            Optimize AVD to fix long vector paths Lint warning

                            ...with SVGOM, but you'll have to check
                            on Android Studio the AVD diff.
                        """.trimIndent(),
                        fontSize = 22.sp
                    )
                }

                Column(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                ) {
                    Text(
                        "SVGO and avocado solve this",
                        style = MaterialTheme.typography.h3,
                        color = VgoColors.OnDark,
                    )
                    Text(
                        "but both require Node.js",
                        style = MaterialTheme.typography.body1,
                        color = VgoColors.Muted,
                    )
                    Text(
                        "…and sometimes don't understand the platform.",
                        style = MaterialTheme.typography.body1,
                        color = VgoColors.Muted,
                    )
                    Caption("issuetracker.google.com/issues/142460503")
                }
            }
        }
    }
}

fun StoryboardBuilder.MotivationCoffee() {
    scene(
        frameCount = 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold {
            Row(
                Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(64.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                ) {
                    GradientText(
                        "A great workflow disappears.",
                        style = MaterialTheme.typography.h3,
                    )
                    Text(
                        "My Niche grinder made morning coffee effortless. The vector shrinking workflow was anything but.",
                        style = MaterialTheme.typography.body1,
                        color = VgoColors.Muted,
                    )
                }
                ScreenshotPlaceholder(
                    "Niche grinder / coffee setup",
                    Modifier.weight(1f).fillMaxHeight(0.7f),
                )
            }
        }
    }
}
