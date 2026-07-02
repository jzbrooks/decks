package com.jzbrooks.dc26.template

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.theme.GradientDivider
import com.jzbrooks.dc26.theme.VgoColors
import dev.bnorm.storyboard.layout.template.Body
import dev.bnorm.storyboard.layout.template.Header
import dev.bnorm.storyboard.layout.template.SceneSection

@Composable
fun SlideScaffold(
    title: (@Composable () -> Unit)? = null,
    badge: String? = null,
    badgeLabel: String? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    Column(
        Modifier.fillMaxSize().padding(horizontal = 64.dp, vertical = 48.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        val header = title ?: SceneSection.title
        Header {
            header()
            if (badge != null) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.CenterEnd),
                ) {
                    if (badgeLabel != null) {
                        Text(badgeLabel, color = VgoColors.Muted, style = MaterialTheme.typography.body2)
                    }
                    OutlinedChip(badge, color = VgoColors.Muted)
                }
            }
        }
        Body(Modifier.padding(top = 24.dp), content)
    }
}
