package com.jzbrooks.deck

import androidx.compose.runtime.*
import com.jzbrooks.deck.youtube.Options
import com.jzbrooks.deck.youtube.PlayerFactory
import com.jzbrooks.deck.youtube.PlayerVars
import com.jzbrooks.deck.youtube.YouTubePlayer
import org.jetbrains.compose.web.dom.Iframe

@Composable
fun EmbeddedYouTubeVideo(
    videoId: String,
    onReady: suspend (YouTubePlayer) -> Unit = {},
) {
    var player by remember { mutableStateOf<YouTubePlayer?>(null) }

    Iframe({
        classes("talk-video")
        attr("src", "https://www.youtube.com/embed/$videoId")
        attr("title", "YouTube video player")
        attr("frameborder", "0")
        attr("referrerpolicy", "strict-origin-when-cross-origin")
        attr("allowfullscreen", "")
    }) {
        DisposableEffect(Unit) {
            player = PlayerFactory(
                maybeElementId = scopeElement,
                options = Options {
                    this.videoId = videoId
                    playerVars = PlayerVars {
                        fs = 0
                    }
                }
            )
            onDispose { player = null }
        }
    }

    LaunchedEffect(player, onReady) {
        onReady(player ?: return@LaunchedEffect)
    }
}
