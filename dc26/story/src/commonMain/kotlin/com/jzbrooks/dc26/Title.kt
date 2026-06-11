package com.jzbrooks.dc26

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate

import androidx.compose.ui.unit.dp
import com.jzbrooks.deck.story.generated.resources.Res
import com.jzbrooks.deck.story.generated.resources.vgo
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.Body
import dev.bnorm.storyboard.layout.template.Header
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import org.jetbrains.compose.resources.painterResource

fun StoryboardBuilder.Title() {
    scene(
        frameCount = 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Header {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Image(
                    painterResource(Res.drawable.vgo),
                    "vgo logo",
                    modifier = Modifier
                        .width(128.dp)
                        .rotate(-20f)
                )
                Text("— R8 for Your Artwork")
            }
        }
        Divider(color = MaterialTheme.colors.primary)
        Body {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Advance scene with right and left arrow keys.")
            }
        }
    }
    }
}