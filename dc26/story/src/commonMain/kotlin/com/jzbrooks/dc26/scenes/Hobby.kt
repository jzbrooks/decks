package com.jzbrooks.dc26.scenes

import androidx.compose.ui.Alignment
import com.jzbrooks.dc26.template.TodoStub
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit

fun StoryboardBuilder.Hobby() {
    scene(
        frameCount = 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        TodoStub(
            "Pandemic hobby story",
            listOf(
                "Origin-story opener — fill in the actual joke",
                "Segue: \"…so naturally, I wrote a vector graphics optimizer\"",
            )
        )
    }
}
