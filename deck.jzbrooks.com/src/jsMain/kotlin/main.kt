import com.jzbrooks.deck.DeckStyleSheet
import com.jzbrooks.deck.Talks
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        Style(DeckStyleSheet)

        Talks()
    }
}

