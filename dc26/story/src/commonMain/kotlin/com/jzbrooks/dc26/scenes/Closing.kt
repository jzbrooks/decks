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
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.resources.Res
import com.jzbrooks.dc26.resources.vgo
import com.jzbrooks.dc26.template.Caption
import com.jzbrooks.dc26.theme.GradientText
import com.jzbrooks.dc26.theme.VgoColors
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import org.jetbrains.compose.resources.painterResource

fun StoryboardBuilder.closing() {
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
                Box(
                    Modifier
                        .background(Color.White, RoundedCornerShape(48.dp))
                        .padding(horizontal = 80.dp, vertical = 40.dp),
                ) {
                    Image(
                        painterResource(Res.drawable.vgo),
                        contentDescription = "vgo logo",
                        modifier = Modifier.width(440.dp),
                    )
                }

                GradientText("github.com/jzbrooks/vgo", style = MaterialTheme.typography.h2)
                Caption("github.com/jzbrooks/vat")
                Text(
                    "Thanks!",
                    style = MaterialTheme.typography.h3,
                    color = VgoColors.OnDark,
                )
            }
        }
    }
}
