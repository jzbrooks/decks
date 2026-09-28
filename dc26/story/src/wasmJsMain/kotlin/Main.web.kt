package com.jzbrooks.dc26.web

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.window.ComposeViewport
import com.jzbrooks.dc26.createStoryboard
import com.jzbrooks.dc26.resources.NotoColorEmoji
import com.jzbrooks.dc26.resources.NotoSansMath
import com.jzbrooks.dc26.resources.Res
import dev.bnorm.storyboard.easel.WebEasel
import org.jetbrains.compose.resources.preloadFont

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport("composeApp") {
        // Remove after CMP 1.12 - fallback is automatic then.
        // Skiko on wasm only bundles Roboto, so emoji (✋ ❌) and symbols
        // outside Inter/JetBrains Mono (⇄) render as tofu unless fallback
        // fonts are registered with the resolver before text is laid out.
        val fontFamilyResolver = LocalFontFamilyResolver.current
        val emojiFont by preloadFont(Res.font.NotoColorEmoji)
        val mathFont by preloadFont(Res.font.NotoSansMath)
        var fallbacksRegistered by remember { mutableStateOf(false) }

        LaunchedEffect(emojiFont, mathFont) {
            val emoji = emojiFont ?: return@LaunchedEffect
            val math = mathFont ?: return@LaunchedEffect
            fontFamilyResolver.preload(FontFamily(emoji))
            fontFamilyResolver.preload(FontFamily(math))
            fallbacksRegistered = true
        }

        if (fallbacksRegistered) {
            MaterialTheme(colors = darkColors()) {
                WebEasel { createStoryboard() }
            }
        }
    }
}
