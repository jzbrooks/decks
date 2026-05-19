package com.jzbrooks.deck.youtube

import kotlin.js.Promise

external interface YouTubePlayer {
    fun getCurrentTime(): Promise<Number>
}
