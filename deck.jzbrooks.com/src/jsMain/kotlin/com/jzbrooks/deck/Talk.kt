package com.jzbrooks.deck

import androidx.compose.runtime.Composable
import com.jzbrooks.deck.layout.FlexColumn
import com.jzbrooks.deck.layout.FlexRow
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Section
import org.jetbrains.compose.web.dom.Text

@Composable
fun Talk(title: String, subtitle: String, video: Video, storyId: String? = null) {
    Section({ classes("talk-card") }) {
        FlexColumn(gap = 32.px) {
            FlexColumn(gap = 4.px, alignItems = AlignItems.Center) {
                P({ classes("talk-title") }) {
                    Text(title)
                }
                P({ classes("talk-subtitle") }) {
                    Text(subtitle)
                }
            }
            FlexRow(gap = 32.px) {
                when (video) {
                    is Video.YouTube -> EmbeddedYouTubeVideo(video.id)
                    is Video.Vimeo -> EmbeddedVimeoVideo(video.id)
                }
                if (storyId != null) {
                    EmbeddedStory(storyId)
                }
            }
        }
    }
}
