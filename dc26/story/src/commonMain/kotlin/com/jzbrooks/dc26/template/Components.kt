package com.jzbrooks.dc26.template

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jzbrooks.dc26.theme.VgoColors
import com.jzbrooks.dc26.theme.VgoGradient
import dev.bnorm.deck.shared.JetBrainsMono

val CodeTextStyle: TextStyle
    @Composable
    get() = TextStyle(fontFamily = JetBrainsMono, fontSize = 40.sp, lineHeight = 64.sp)

@Composable
fun Chip(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    contentColor: Color = VgoColors.Background,
) {
    Box(
        modifier
            .background(color, RoundedCornerShape(16.dp))
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Text(text, color = contentColor, style = MaterialTheme.typography.body2, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun OutlinedChip(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
) {
    Box(
        modifier
            .border(4.dp, color, RoundedCornerShape(16.dp))
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Text(text, color = color, style = MaterialTheme.typography.body2, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun GradientChip(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier
            .background(VgoGradient, RoundedCornerShape(16.dp))
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Text(
            text,
            color = Color.White,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
fun ByteChip(bytes: Int, modifier: Modifier = Modifier, color: Color = MaterialTheme.colors.secondary) {
    OutlinedChip("$bytes B", modifier, color)
}

@Composable
fun Caption(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        modifier = modifier,
        color = VgoColors.Muted,
        style = MaterialTheme.typography.body1,
        fontStyle = FontStyle.Italic,
    )
}

// A quoted aside, set off with a vertical rule like a markdown blockquote.
@Composable
fun Blockquote(text: String, modifier: Modifier = Modifier) {
    Row(modifier.height(IntrinsicSize.Min)) {
        Box(
            Modifier
                .fillMaxHeight()
                .width(4.dp)
                .background(VgoColors.Muted, RoundedCornerShape(2.dp))
        )
        Text(
            text,
            modifier = Modifier.padding(start = 24.dp),
            color = VgoColors.Muted,
            style = MaterialTheme.typography.body1,
            fontStyle = FontStyle.Italic,
        )
    }
}

// A numbered step in a "how it works" walkthrough.
@Composable
fun AlgorithmStep(label: String, detail: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            label,
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.SemiBold,
            color = VgoColors.Azure,
        )
        Text(detail, style = MaterialTheme.typography.body2, color = VgoColors.Muted)
    }
}
