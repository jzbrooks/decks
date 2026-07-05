package com.jzbrooks.dc26.scenes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jzbrooks.dc26.template.Blockquote
import com.jzbrooks.dc26.template.SlideScaffold
import com.jzbrooks.dc26.template.Terminal
import com.jzbrooks.dc26.theme.VgoColors
import com.jzbrooks.deck.story.generated.resources.Res
import com.jzbrooks.deck.story.generated.resources.ic_add_a_profile_megaphone_image
import com.jzbrooks.deck.story.generated.resources.ic_notification_permission
import com.jzbrooks.deck.story.generated.resources.ic_onboarding
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import dev.bnorm.storyboard.toValue
import org.jetbrains.compose.resources.painterResource

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
                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(color = VgoColors.Muted)) { append("% ") }
                            append("git log -1 377d3be")
                        },
                        fontSize = 22.sp,
                    )
                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(color = VgoColors.Amber)) {
                                append("commit 377d3bef0cef20984dfab154bc2ad0c597b7c0b8")
                            }
                            append(
                                """
                                |
                                |Author: Simon Marquis <contact@simon-marquis.fr>
                                |Date:   Fri Jun  2 21:43:25 2023 +0000
                                |
                                |    Optimize AVD to fix long vector paths Lint warning
                                |
                                |    ...with SVGOM, but you'll have to check
                                |    on Android Studio the AVD diff.
                                """.trimMargin()
                            )
                        },
                        fontSize = 22.sp,
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
                    Blockquote("% If you get this error after pre-processing an SVG with SVGOMG…\nissuetracker.google.com/issues/142460503")
                }
            }
        }
    }
}

fun StoryboardBuilder.MotivationMultipleFormats() {
    scene(
        frameCount = 2,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Terminal(Modifier.fillMaxSize(0.7f), title = "app — zsh") {
                    ProvideTextStyle(LocalTextStyle.current.copy(fontSize = 28.sp, lineHeight = 42.sp)) {
                        Text(buildAnnotatedString {
                            withStyle(SpanStyle(color = VgoColors.Muted)) { append("% ") }
                            append("svgo src/main/res/drawable/network-error.xml")
                        })

                        transition.AnimatedVisibility(
                            visible = { it.toValue() >= 1 },
                            enter = expandVertically() + fadeIn(),
                            exit = fadeOut(),
                        ) {
                            Column {
                                Spacer(Modifier.height(32.dp))
                                Text(buildAnnotatedString {
                                    withStyle(SpanStyle(color = VgoColors.Muted)) { append("% ") }
                                    append("svgo assets/network-error.pdf")
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}
