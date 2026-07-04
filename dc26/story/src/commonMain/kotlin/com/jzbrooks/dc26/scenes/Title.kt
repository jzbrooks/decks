package com.jzbrooks.dc26.scenes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.template.Caption
import com.jzbrooks.dc26.theme.GradientText
import com.jzbrooks.dc26.theme.VgoColors
import com.jzbrooks.dc26.theme.VgoTheme
import com.jzbrooks.deck.story.generated.resources.Res
import com.jzbrooks.deck.story.generated.resources.google_icons
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
                Image(
                    painterResource(Res.drawable.google_icons),
                    contentDescription = "sheet of material icons",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxSize(),
                    colorFilter = ColorFilter.tint(
                        color = Color.Black.copy(alpha = 0.1f),
                        blendMode = BlendMode.SrcOver
                    )
                )
            }

            Column(
                modifier = Modifier.background(
                    color = VgoColors.Surface,
                    shape = RoundedCornerShape(8.dp),
                ).padding(32.dp)
            ) {
                GradientText(
                    "Shrinking Vector Art",
                    style = MaterialTheme.typography.h2,
                    textAlign = TextAlign.Center,
                )

                Text(
                    "Justin Brooks",
                    style = MaterialTheme.typography.h3,
                    color = VgoColors.OnDark,
                )
            }
        }
    }
}
