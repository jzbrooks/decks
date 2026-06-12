package com.jzbrooks.dc26.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.bnorm.deck.shared.Inter
import dev.bnorm.storyboard.ContentDecorator

// Palette riffing on the vgo logo gradient (#B125EA -> #833FEF -> #008AFF).
object VgoColors {
    val Magenta = Color(0xFFB125EA)
    val Violet = Color(0xFF833FEF)
    val Azure = Color(0xFF008AFF)

    // The logo's construction-line blue, lightened to read on a dark background.
    val Handle = Color(0xFF5E81FF)

    val Background = Color(0xFF120E1C)
    val Surface = Color(0xFF1D1730)
    val OnDark = Color(0xFFECE6F8)
    val Muted = Color(0xFF8E85A3)

    // Deliberately outside the gradient family: marks "before" states and warnings.
    val Amber = Color(0xFFFFC857)

    val GridLine = OnDark.copy(alpha = 0.08f)
    val Axis = OnDark.copy(alpha = 0.20f)
    val PathFill = Violet.copy(alpha = 0.25f)
}

val VgoGradient = Brush.linearGradient(
    colors = listOf(VgoColors.Magenta, VgoColors.Violet, VgoColors.Azure),
)

val VgoTheme = ContentDecorator { content ->
    val colors = darkColors(
        background = VgoColors.Background,
        surface = VgoColors.Surface,
        onBackground = VgoColors.OnDark,
        onSurface = VgoColors.OnDark,
        primary = Color(0xFFC79BF7),
        primaryVariant = VgoColors.Violet,
        secondary = Color(0xFF61B3FF),
        secondaryVariant = VgoColors.Azure,
    )

    val typography = Typography(
        defaultFontFamily = Inter,
        h1 = TextStyle(fontSize = 72.sp, fontWeight = FontWeight.Bold),
        h2 = TextStyle(fontSize = 56.sp, fontWeight = FontWeight.Bold),
        h3 = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.SemiBold),
        h4 = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.SemiBold),
        body1 = TextStyle(fontSize = 20.sp),
        body2 = TextStyle(fontSize = 16.sp),
        caption = TextStyle(fontSize = 14.sp),
    )

    MaterialTheme(colors, typography) {
        Surface {
            content()
        }
    }
}
