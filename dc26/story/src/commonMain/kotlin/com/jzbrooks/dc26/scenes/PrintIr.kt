package com.jzbrooks.dc26.scenes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.jzbrooks.dc26.template.Terminal
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit

// One Dark-ish accents matching the Terminal chrome.
private val Muted = Color(0xFF7F848E)
private val Amber = Color(0xFFE5C07B)
private val FillSwatch = Color(0xFF000000)
private val StrokeSwatch = Color(0xFFFF0000)

private val COMMANDS = listOf(
    "M" to "10,30",
    "a" to "20.008137,20.008137 0 0,1 5.858,-14.142",
    "A" to "20.008137,20.008137 0 0,1 30,10",
    "a" to "20.008123,20.008123 0 0,1 14.141998,5.858",
    "A" to "20.008133,20.008133 0 0,1 50,30",
    "a" to "20.008135,20.008135 0 0,1 5.8580017,-14.142",
    "A" to "20.008116,20.008116 0 0,1 70,10",
    "a" to "20.008123,20.008123 0 0,1 14.141998,5.858",
    "A" to "20.008133,20.008133 0 0,1 90,30",
    "q" to "0,30 -40,60",
    "Q" to "10,60 10,30",
)

fun StoryboardBuilder.PrintIr() {
    scene(
        frameCount = 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Terminal(Modifier.fillMaxWidth(0.9f), title = "vgo — zsh") {
                ProvideTextStyle(LocalTextStyle.current.copy(fontSize = 28.sp, lineHeight = 42.sp)) {
                    Text(buildAnnotatedString {
                        withStyle(SpanStyle(color = Muted)) { append("% ") }
                        append("vgo --print-ir vgo/src/test/resources/simple_heart.xml")
                    })

                    Text(buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("VectorDrawable") }
                        withStyle(SpanStyle(color = Muted)) { append(" [vector] (100x100)") }
                    })

                    Text(buildAnnotatedString {
                        withStyle(SpanStyle(color = Muted)) { append("└── ") }
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Path") }
                        withStyle(SpanStyle(color = Muted)) { append(" [path]") }
                        append(" fill=")
                        withStyle(SpanStyle(color = FillSwatch)) { append("■") }
                        withStyle(SpanStyle(color = Amber)) { append(" #00000000") }
                        append(" stroke=")
                        withStyle(SpanStyle(color = StrokeSwatch)) { append("■") }
                        withStyle(SpanStyle(color = Amber)) { append(" #ff0000") }
                        append(" sw=1")
                        withStyle(SpanStyle(color = Muted)) { append(" (11 cmds)") }
                    })

                    for ((index, command) in COMMANDS.withIndex()) {
                        val branch = if (index == COMMANDS.lastIndex) "└── " else "├── "
                        Text(commandLine(branch, command.first, command.second))
                    }
                }
            }
        }
    }
}

private fun commandLine(branch: String, letter: String, args: String): AnnotatedString =
    buildAnnotatedString {
        withStyle(SpanStyle(color = Muted)) { append("    $branch") }
        withStyle(SpanStyle(color = Amber)) { append(letter) }
        append(" $args")
    }
