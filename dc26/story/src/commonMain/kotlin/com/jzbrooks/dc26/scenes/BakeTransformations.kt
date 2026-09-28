package com.jzbrooks.dc26.scenes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.createChildTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ProvideTextStyle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.template.ByteChip
import com.jzbrooks.dc26.template.Caption
import com.jzbrooks.dc26.template.CodeTextStyle
import com.jzbrooks.dc26.template.SlideScaffold
import com.jzbrooks.dc26.theme.DC26_XML
import com.jzbrooks.dc26.theme.VgoColors
import com.jzbrooks.dc26.vector.Close
import com.jzbrooks.dc26.vector.LineTo
import com.jzbrooks.dc26.vector.MoveTo
import com.jzbrooks.dc26.vector.PathCanvas
import com.jzbrooks.dc26.vector.PathCommand
import com.jzbrooks.dc26.vector.Point
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import dev.bnorm.storyboard.text.highlight.style
import dev.bnorm.storyboard.text.magic.MagicText
import dev.bnorm.storyboard.toValue

private val BEFORE_XML =
    """
    <group
        android:rotation="45"
        android:pivotX="12"
        android:pivotY="12">
      <path android:pathData="M6,12h12v4h-12z"/>
    </group>
    """.trimIndent()

private val AFTER_XML =
    """
    <group>
      <path android:pathData="M7.8,7.8L16.2,16.2 13.4,19.1 4.9,10.6Z"/>
    </group>
    """.trimIndent()

private val AFTER_XML_DISPLAY =
    """
    <group>
      <path
          android:pathData="M7.8,7.8
              L16.2,16.2 13.4,19.1
              4.9,10.6Z"/>
    </group>
    """.trimIndent()

// The rectangle above with its rotation folded into the coordinates.
private val BAKED: List<PathCommand> =
    listOf(
        MoveTo(Point(7.76f, 7.76f)),
        LineTo(Point(16.24f, 16.24f)),
        LineTo(Point(13.41f, 19.07f)),
        LineTo(Point(4.93f, 10.59f)),
        Close,
    )

fun StoryboardBuilder.bakeTransformations() {
    scene(
        frameCount = 2,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold(badge = "Top-down", badgeLabel = "Bake Transformations") {
            val frame = transition.currentState.toValue()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(96.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f),
                ) {
                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.weight(1f),
                    ) {
                        ProvideTextStyle(CodeTextStyle) {
                            val xml =
                                transition.createChildTransition {
                                    (if (it.toValue() >= 1) AFTER_XML_DISPLAY else BEFORE_XML).style(DC26_XML)
                                }
                            MagicText(xml)
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.padding(top = 48.dp).weight(1f),
                    ) {
                        AnimatedVisibility(visible = frame >= 1, enter = fadeIn(), exit = fadeOut()) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(32.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                ByteChip(BEFORE_XML.length, color = VgoColors.Amber)
                                Caption("→")
                                ByteChip(AFTER_XML.length)
                            }
                        }
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    PathCanvas(
                        commands = BAKED,
                        viewport = Rect(0f, 0f, 24f, 24f),
                        fill = VgoColors.PathFill,
                        modifier =
                            Modifier
                                .size(640.dp)
                                .clip(RoundedCornerShape(32.dp))
                                .background(VgoColors.Surface),
                    )
                    Caption("pre-apply transformations to path data")
                }
            }
        }
    }
}
