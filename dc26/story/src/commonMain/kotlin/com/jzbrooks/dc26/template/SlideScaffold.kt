package com.jzbrooks.dc26.template

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.theme.GradientDivider
import dev.bnorm.storyboard.layout.template.Body
import dev.bnorm.storyboard.layout.template.Header
import dev.bnorm.storyboard.layout.template.SceneSection

@Composable
fun SlideScaffold(
    title: (@Composable () -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    Column(
        Modifier.fillMaxSize().padding(horizontal = 64.dp, vertical = 48.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        val header = title ?: SceneSection.title
        Header { header() }
        Body(Modifier.padding(top = 24.dp), content)
    }
}
