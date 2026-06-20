package com.jzbrooks.dc26.scenes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.template.Caption
import com.jzbrooks.dc26.theme.GradientText
import com.jzbrooks.deck.story.generated.resources.Res
import com.jzbrooks.deck.story.generated.resources.vgo
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import org.jetbrains.compose.resources.painterResource

fun StoryboardBuilder.Title() {
    scene(
        frameCount = 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(56.dp),
            ) {
                // The logo was drawn for a light background; give it its own card.
                Box(
                    Modifier
                        .background(Color.White, RoundedCornerShape(48.dp))
                        .padding(horizontal = 96.dp, vertical = 48.dp)
                ) {
                    Image(
                        painterResource(Res.drawable.vgo),
                        contentDescription = "vgo logo",
                        modifier = Modifier.width(560.dp),
                    )
                }

                GradientText(
                    "Shrinking Vector Art",
                    style = MaterialTheme.typography.h2,
                    textAlign = TextAlign.Center,
                )
                Caption("Justin Brooks")
            }
        }
    }
}
