package com.jzbrooks.dc26.template

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bnorm.deck.shared.JetBrainsMono
import dev.bnorm.storyboard.Frame
import dev.bnorm.storyboard.SceneScope

// A dark, modern macOS terminal window: charcoal chrome that blends into the
// content area, mono text, standard traffic lights.
private val TerminalBackground = Color(0xFF282C34)
private val TerminalTitlebar = Color(0xFF2F343D)
private val TerminalForeground = Color(0xFFFFFFFF)
private val TerminalTitleText = Color(0xFFB9BFC9)

@Composable
fun Terminal(
    modifier: Modifier = Modifier,
    title: String = "~ — zsh",
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(12.dp)
    Column(
        modifier
            .shadow(24.dp, shape)
            .clip(shape)
            .background(TerminalBackground)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(34.dp)
                .background(TerminalTitlebar)
        ) {
            Row(
                Modifier.align(Alignment.CenterStart).padding(start = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TrafficLight(Color(0xFFFF5F57))
                Spacer(Modifier.size(8.dp))
                TrafficLight(Color(0xFFFEBC2E))
                Spacer(Modifier.size(8.dp))
                TrafficLight(Color(0xFF28C840))
            }
            Text(
                title,
                color = TerminalTitleText,
                fontSize = 13.sp,
                modifier = Modifier.align(Alignment.Center),
            )
        }

        ProvideTextStyle(
            TextStyle(fontFamily = JetBrainsMono, color = TerminalForeground, fontSize = 18.sp)
        ) {
            Column(Modifier.fillMaxWidth().padding(18.dp)) {
                content()
            }
        }
    }
}

/**
 * A wide terminal that peeks in from the bottom edge of the slide,
 * its lower corners just off-screen.
 */
@Composable
fun <T> SceneScope<T>.TerminalPopup(
    visible: (Frame<T>) -> Boolean,
    title: String = "~ — zsh",
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        transition.AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(400, easing = EaseIn),
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(400, easing = EaseOut),
            ),
        ) {
            Terminal(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .offset(y = 18.dp),
                title = title,
                content = content,
            )
        }
    }
}

@Composable
private fun TrafficLight(color: Color) {
    Box(Modifier.size(12.dp).background(color, CircleShape))
}
