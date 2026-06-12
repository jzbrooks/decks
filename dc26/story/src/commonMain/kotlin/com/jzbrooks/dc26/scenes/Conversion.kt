package com.jzbrooks.dc26.scenes

import androidx.compose.animation.core.createChildTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.template.Caption
import com.jzbrooks.dc26.template.CodeTextStyle
import com.jzbrooks.dc26.template.GradientChip
import com.jzbrooks.dc26.template.OutlinedChip
import com.jzbrooks.dc26.template.SlideScaffold
import com.jzbrooks.dc26.theme.DC26_XML
import com.jzbrooks.dc26.theme.VgoColors
import com.jzbrooks.dc26.theme.VgoGradient
import dev.bnorm.deck.shared.INTELLIJ_DARK
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import dev.bnorm.storyboard.text.highlight.Language
import dev.bnorm.storyboard.text.highlight.highlight
import dev.bnorm.storyboard.text.highlight.style
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.toValue

private val SVG = """
    <svg viewBox="0 0 24 24">
      <path fill="#833FEF" d="M9,18V6l8,6z"/>
    </svg>
""".trimIndent()

private val VECTOR_DRAWABLE = """
    <vector
        android:viewportWidth="24"
        android:viewportHeight="24">
      <path
          android:fillColor="#833FEF"
          android:pathData="M9,18V6l8,6z"/>
    </vector>
""".trimIndent()

private val IMAGE_VECTOR = """
    ImageVector.Builder(
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).path(fill = SolidColor(Color(0xFF833FEF))) {
        moveTo(9f, 18f)
        verticalLineTo(6f)
        lineToRelative(8f, 6f)
        close()
    }.build()
""".trimIndent()

fun StoryboardBuilder.Conversion() {
    scene(
        frameCount = 4,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold {
            val frame = transition.currentState.toValue()

            Column(verticalArrangement = Arrangement.spacedBy(28.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedChip("SVG", color = if (frame == 0) VgoColors.Azure else VgoColors.Muted)
                    Text("⇄", style = MaterialTheme.typography.h3, color = VgoColors.Muted)
                    Box(
                        if (frame >= 3) Modifier.border(3.dp, VgoGradient, RoundedCornerShape(12.dp))
                        else Modifier
                    ) {
                        GradientChip("IR", Modifier.padding(4.dp))
                    }
                    Text("⇄", style = MaterialTheme.typography.h3, color = VgoColors.Muted)
                    OutlinedChip("VectorDrawable", color = if (frame == 1) VgoColors.Azure else VgoColors.Muted)
                    Text("·", style = MaterialTheme.typography.h3, color = VgoColors.Muted)
                    OutlinedChip("ImageVector", color = if (frame == 2) VgoColors.Azure else VgoColors.Muted)
                }

                Box(Modifier.height(280.dp)) {
                    ProvideTextStyle(CodeTextStyle) {
                        val code = transition.createChildTransition {
                            when (it.toValue()) {
                                0 -> SVG.style(DC26_XML)
                                1 -> VECTOR_DRAWABLE.style(DC26_XML)
                                else -> IMAGE_VECTOR.highlight(INTELLIJ_DARK, Language.Kotlin)
                            }
                        }
                        MagicText(code)
                    }
                }

                if (frame >= 3) {
                    Caption("one drawing, three dialects — every reader and writer meets in the same IR")
                }
            }
        }
    }
}
