package com.jzbrooks.dc26.scenes

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.resources.Res
import com.jzbrooks.dc26.resources.vgo
import com.jzbrooks.dc26.template.SlideScaffold
import com.jzbrooks.dc26.theme.VgoColors
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.toValue
import org.jetbrains.compose.resources.painterResource

// Tokenized by hand so "vg" and "optimizer" stay shared across frames:
// only the leading "s" swaps for "❌" while the rest glides into place.
private fun name(crossed: Boolean): List<AnnotatedString> =
    listOf(
        if (crossed) {
            AnnotatedString("❌")
        } else {
            AnnotatedString("s", SpanStyle(color = VgoColors.Magenta))
        },
        AnnotatedString(
            "vg",
            SpanStyle(brush = Brush.linearGradient(listOf(VgoColors.Magenta, VgoColors.Violet))),
        ),
        AnnotatedString(" "),
        AnnotatedString(
            "optimizer",
            SpanStyle(brush = Brush.linearGradient(listOf(VgoColors.Violet, VgoColors.Azure))),
        ),
    )

fun StoryboardBuilder.vgoName() {
    scene(
        frameCount = 3,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold {
            val tokens = transition.createChildTransition { name(crossed = it.toValue() >= 1) }
            val showLogo = transition.createChildTransition { it.toValue() >= 2 }

            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Box(
                    Modifier
                        .background(Color.White, RoundedCornerShape(48.dp))
                        .padding(horizontal = 80.dp, vertical = 48.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    showLogo.AnimatedContent(
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                    ) { logo ->
                        if (logo) {
                            Image(
                                painterResource(Res.drawable.vgo),
                                contentDescription = "vgo logo",
                                modifier = Modifier.width(440.dp),
                            )
                        } else {
                            ProvideTextStyle(MaterialTheme.typography.h2) {
                                MagicText(tokens)
                            }
                        }
                    }
                }
            }
        }
    }
}
