package com.jzbrooks.dc26.scenes

import com.jzbrooks.dc26.template.TodoStubScene
import dev.bnorm.storyboard.StoryboardBuilder

fun StoryboardBuilder.DecompileApk() {
    TodoStubScene(
        "Inside a real APK",
        "Decompile an APK and inspect its VectorDrawables — flesh out this demo",
        "Decide: live demo vs. recorded/static terminal frames",
    )
}
