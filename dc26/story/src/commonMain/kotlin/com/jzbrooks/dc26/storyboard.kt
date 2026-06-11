package com.jzbrooks.dc26

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Typography
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import dev.bnorm.storyboard.ContentDecorator
import dev.bnorm.storyboard.SceneFormat
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.layout.template.section

fun createStoryboard(): Storyboard {
    return Storyboard.build(
        title = "vgo: A Vector Optimizer Built Like a Compiler",
        format = SceneFormat.Default,
        decorator = theme,
    ) {
        Title()
    }
}

private val theme = ContentDecorator { content ->
    val colors = lightColors(
        background = Color.White,
        surface = Color(0xFFF7F5FF),
        onBackground = Color(0xFFF7F5FF),
        primary = Color(0xFFC9A8FF),
        primaryVariant = Color(0xA8B8FF),
        secondary = Color(0xFF90C8FF),
    )

    val typography = Typography()

    MaterialTheme(colors, typography) {
        Surface {
            content()
        }
    }
}
