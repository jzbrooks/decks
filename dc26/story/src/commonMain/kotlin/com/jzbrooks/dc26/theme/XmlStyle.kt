package com.jzbrooks.dc26.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import dev.bnorm.storyboard.text.highlight.XmlHighlighting

// IntelliJ-dark-flavored XML colors for SVG/VectorDrawable snippets.
val DC26_XML = XmlHighlighting.build(base = SpanStyle(color = VgoColors.OnDark)) {
    tag = SpanStyle(color = Color(0xFFBCBEC4))
    tagName = SpanStyle(color = Color(0xFFE8BF6A))
    attributeName = SpanStyle(color = Color(0xFFBCBEC4))
    attributeValue = SpanStyle(color = Color(0xFF6AAB73))
    comment = SpanStyle(color = Color(0xFF7A7E85))
    prologue = SpanStyle(color = Color(0xFF7A7E85))
    entityReference = SpanStyle(color = Color(0xFF2AACB8))
    tagData = SpanStyle(color = VgoColors.OnDark)
}
