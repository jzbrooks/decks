package com.jzbrooks.dc26.scenes

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.template.ByteChip
import com.jzbrooks.dc26.template.Caption
import com.jzbrooks.dc26.template.CodeTextStyle
import com.jzbrooks.dc26.template.SlideScaffold
import com.jzbrooks.dc26.theme.VgoColors
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.toValue

private class WriterStep(val text: String, val label: String)

private val WRITER_STEPS = listOf(
    WriterStep("L 1.200000 -3.400000", "as exported by a design tool"),
    WriterStep("L 1.2 -3.4", "precision capped at 3 digits"),
    WriterStep("L1.2-3.4", "separators elided — the minus sign is separator enough"),
)

fun StoryboardBuilder.OptimizationCategories() {
    scene(
        frames = WRITER_STEPS,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold {
            Column(verticalArrangement = Arrangement.spacedBy(80.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(64.dp),
                    modifier = Modifier.height(IntrinsicSize.Max),
                ) {
                    CategoryCard(
                        title = "IR optimizations",
                        subtitle = "restructure the drawing",
                        examples = "merge paths · bake transforms · rewrite commands",
                        color = VgoColors.Violet,
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                    )
                    CategoryCard(
                        title = "Writer optimizations",
                        subtitle = "respell the text",
                        examples = "precision · separators · default elision",
                        color = VgoColors.Azure,
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                    )
                }

                val step = transition.currentState.toValue()
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                ) {
                    ProvideTextStyle(CodeTextStyle.copy(fontSize = MaterialTheme.typography.h3.fontSize)) {
                        val text = transition.createChildTransition { AnnotatedString(it.toValue().text) }
                        MagicText(text)
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ByteChip(step.text.length)
                        Caption(step.label)
                    }
                }
            }
        }
    }
}

@androidx.compose.runtime.Composable
private fun CategoryCard(
    title: String,
    subtitle: String,
    examples: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .border(4.dp, color.copy(alpha = 0.6f), RoundedCornerShape(32.dp))
            .background(VgoColors.Surface, RoundedCornerShape(32.dp))
            .padding(48.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(title, style = MaterialTheme.typography.h3, color = color)
        Text(subtitle, style = MaterialTheme.typography.h4)
        Caption(examples)
    }
}
