package com.jzbrooks.dc26.scenes

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
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
        Column {
            Text("Around 2018 I got really into coffee... and vector graphics")
            Text("I bought a Niche grinder because the workflow was incredible, but then I realized my vector shrinking workflow was a PITA")
            // todo: didn't want to introduce a node dependency on our build for svgo
            // todo: link / screenshot to now in android commit about vector path length warning
            // todo: learned about avocado but still the node dependency problem
            Text("I was working on a project that had a lot of large vector graphics for empty and error states. Shrinking had a significant effect on app size.")
            Text("What's more, I realized our iOS team was shipping the same graphics as PDFs, unshrunk")
            Text("We can do better...")

            Text("So I thought... I studied math in school. Surely I can do this... but the joke was on me because I had forgotten nearly all of it.")
        }
    }
}
