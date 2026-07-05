package com.jzbrooks.dc26.scenes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jzbrooks.dc26.template.Caption
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

private enum class Traversal { TopDown, BottomUp }

private class Transform(val name: String, val traversal: Traversal, val starred: Boolean = false)

private val TRANSFORMATIONS = listOf(
    Transform("ConvertShapesToPaths", traversal = Traversal.TopDown),
    Transform("RemoveTransparentPaths", traversal = Traversal.TopDown),
    Transform("BakeTransformations", traversal = Traversal.TopDown, starred = true),
    Transform("BreakoutImplicitCommands", traversal = Traversal.TopDown),
    Transform("CommandVariant(Relative)", traversal = Traversal.TopDown, starred = true),
    Transform("ConvertCurvesToArcs", traversal = Traversal.TopDown, starred = true),
    Transform("SimplifyBezierCurveCommands", traversal = Traversal.TopDown, starred = true),
    Transform("SimplifyLineCommands", traversal = Traversal.TopDown, starred = true),
    Transform("RemoveRedundantCommands", traversal = Traversal.TopDown, starred = true),
    Transform("CommandVariant(Compact)", traversal = Traversal.TopDown, starred = true),
    Transform("Polycommands", traversal = Traversal.TopDown),
    Transform("CollapseGroups", traversal = Traversal.BottomUp),
    Transform("RemoveEmptyGroups", traversal = Traversal.BottomUp),
    Transform("MergePaths", traversal = Traversal.BottomUp, starred = true),
)

private val TOP_DOWN_TRANSFORMS = TRANSFORMATIONS.filter { it.traversal == Traversal.TopDown }
private val BOTTOM_UP_TRANSFORMS = TRANSFORMATIONS.filter { it.traversal == Traversal.BottomUp }

fun StoryboardBuilder.Pipeline() {
    scene(
        frameCount = 4,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold {
            val frame = transition.currentState.toValue()

            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(48.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedChip("IR", color = VgoColors.Muted)

                    Arrow()

                    Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                        PassCluster(
                            traversal = Traversal.TopDown,
                            transforms = TOP_DOWN_TRANSFORMS,
                            highlightStars = frame >= 1,
                            grouped = frame >= 2,
                        )

                        PassCluster(
                            traversal = Traversal.BottomUp,
                            transforms = BOTTOM_UP_TRANSFORMS,
                            highlightStars = frame >= 1,
                            grouped = frame >= 2,
                        )
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

                AnimatedVisibility(
                    visible = frame >= 3,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut(),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        OutlinedChip("O(n)", color = VgoColors.Azure)
                        Caption(
                            "${TRANSFORMATIONS.size} transformations, ${Traversal.entries.size} tree traversals — " +
                                "cost scales with graphic elements, not the number of transformations applied"
                        )
                    }
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
private fun PassCluster(
    traversal: Traversal,
    transforms: List<Transform>,
    highlightStars: Boolean,
    grouped: Boolean,
) {
    val borderColor by animateColorAsState(if (grouped) VgoColors.Azure else Color.Transparent)
    val padding by animateDpAsState(if (grouped) 12.dp else 0.dp)
    val labelAlpha by animateFloatAsState(if (grouped) 1f else 0f)

    Column {
        Caption(
            text = when (traversal) {
                Traversal.TopDown -> "top-down traversal"
                Traversal.BottomUp -> "bottom-up traversal"
            },
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp).alpha(labelAlpha),
        )
        Column(
            Modifier
                .border(3.dp, borderColor, RoundedCornerShape(16.dp))
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            for (transform in transforms) {
                TransformPill(transform, highlightStars = highlightStars)
            }
        }
    }
}

@Composable
private fun TransformPill(transform: Transform, highlightStars: Boolean) {
    val highlighted = highlightStars && transform.starred
    val background by animateColorAsState(
        if (highlighted) VgoColors.Violet else VgoColors.Surface,
    )
    Box(
        Modifier
            .background(background, RoundedCornerShape(12.dp))
            .padding(horizontal = 20.dp, vertical = 4.dp)
    ) {
        Text(
            transform.name,
            fontSize = 26.sp,
            fontFamily = JetBrainsMono,
            fontWeight = if (highlighted) FontWeight.SemiBold else FontWeight.Normal,
            color = if (highlighted) Color.White else VgoColors.OnDark,
        )
    }
}
