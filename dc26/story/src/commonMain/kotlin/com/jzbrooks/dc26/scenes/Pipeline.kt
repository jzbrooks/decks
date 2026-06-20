package com.jzbrooks.dc26.scenes

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jzbrooks.dc26.template.OutlinedChip
import com.jzbrooks.dc26.template.SlideScaffold
import com.jzbrooks.dc26.theme.GradientText
import com.jzbrooks.dc26.theme.VgoColors
import com.jzbrooks.dc26.theme.VgoGradient
import dev.bnorm.deck.shared.JetBrainsMono
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import dev.bnorm.storyboard.toValue

private class Pass(val name: String, val starred: Boolean = false)

private val PASSES = listOf(
    Pass("ConvertShapesToPaths"),
    Pass("RemoveTransparentPaths"),
    Pass("BakeTransformations", starred = true),
    Pass("BreakoutImplicitCommands"),
    Pass("CommandVariant(Relative)", starred = true),
    Pass("ConvertCurvesToArcs", starred = true),
    Pass("SimplifyBezierCurveCommands", starred = true),
    Pass("SimplifyLineCommands", starred = true),
    Pass("RemoveRedundantCommands", starred = true),
    Pass("CommandVariant(Compact)", starred = true),
    Pass("Polycommands"),
    Pass("CollapseGroups"),
    Pass("RemoveEmptyGroups"),
    Pass("MergePaths", starred = true),
)

fun StoryboardBuilder.Pipeline() {
    scene(
        frameCount = 2,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold {
            val frame = transition.currentState.toValue()

            Row(
                horizontalArrangement = Arrangement.spacedBy(48.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedChip("IR", color = VgoColors.Muted)

                Arrow()

                Column(
                    Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    for (pass in PASSES) {
                        PassPill(pass, highlightStars = frame >= 1)
                    }
                }

                Arrow()

                Box(
                    Modifier
                        .border(4.dp, VgoGradient, RoundedCornerShape(16.dp))
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    GradientText("Optimized IR", style = MaterialTheme.typography.body2)
                }
            }
        }
    }
}

@Composable
private fun Arrow() {
    Text("→", style = MaterialTheme.typography.h2, color = VgoColors.Muted)
}

@Composable
private fun PassPill(pass: Pass, highlightStars: Boolean) {
    val highlighted = highlightStars && pass.starred
    val background by animateColorAsState(
        if (highlighted) VgoColors.Violet else VgoColors.Surface,
    )
    Box(
        Modifier
            .background(background, RoundedCornerShape(12.dp))
            .padding(horizontal = 20.dp, vertical = 4.dp)
    ) {
        Text(
            pass.name,
            fontSize = 26.sp,
            fontFamily = JetBrainsMono,
            fontWeight = if (highlighted) FontWeight.SemiBold else FontWeight.Normal,
            color = if (highlighted) androidx.compose.ui.graphics.Color.White else VgoColors.OnDark,
        )
    }
}
