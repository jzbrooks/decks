package com.jzbrooks.dc26.vector

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

/**
 * Makes logical-coordinate points draggable on a canvas that uses the same
 * [ViewportTransform] mapping as [PathCanvas]. Keyboard navigation is untouched;
 * only pointer drags are consumed.
 */
fun Modifier.dragHandles(
    viewport: Rect,
    handles: () -> List<Point>,
    hitRadius: Dp = 48.dp,
    onMove: (index: Int, position: Point) -> Unit,
): Modifier =
    pointerInput(viewport) {
        var active = -1
        detectDragGestures(
            onDragStart = { down ->
                val transform = ViewportTransform(viewport, size.toSize())
                val radius = hitRadius.toPx()
                active = handles()
                    .withIndex()
                    .filter { (transform.toScreen(it.value) - down).getDistance() <= radius }
                    .minByOrNull { (transform.toScreen(it.value) - down).getDistance() }
                    ?.index ?: -1
            },
            onDragEnd = { active = -1 },
            onDragCancel = { active = -1 },
        ) { change, _ ->
            if (active >= 0) {
                change.consume()
                val transform = ViewportTransform(viewport, size.toSize())
                onMove(active, transform.toViewport(change.position))
            }
        }
    }
