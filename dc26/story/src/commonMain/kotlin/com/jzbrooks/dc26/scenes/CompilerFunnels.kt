package com.jzbrooks.dc26.scenes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.jzbrooks.dc26.resources.Res
import com.jzbrooks.dc26.resources.android
import com.jzbrooks.dc26.resources.java
import com.jzbrooks.dc26.resources.kmp
import com.jzbrooks.dc26.resources.kotlin
import com.jzbrooks.dc26.resources.llvm
import com.jzbrooks.dc26.resources.mono_gorilla
import com.jzbrooks.dc26.resources.rust
import com.jzbrooks.dc26.resources.swift
import com.jzbrooks.dc26.template.Caption
import com.jzbrooks.dc26.template.OutlinedChip
import com.jzbrooks.dc26.template.SlideScaffold
import com.jzbrooks.dc26.theme.GradientText
import com.jzbrooks.dc26.theme.VgoColors
import com.jzbrooks.dc26.theme.VgoGradient
import dev.bnorm.storyboard.StoryboardBuilder
import dev.bnorm.storyboard.layout.template.SceneEnter
import dev.bnorm.storyboard.layout.template.SceneExit
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

private class FunnelNode(
    val label: String,
    val logos: List<DrawableResource> = emptyList(),
) {
    constructor(label: String, logo: DrawableResource) : this(label, listOf(logo))
}

fun StoryboardBuilder.r8Funnel() {
    scene(
        frameCount = 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold {
            FunnelDiagram(
                inputs =
                    listOf(
                        FunnelNode("class files", listOf(Res.drawable.java, Res.drawable.kotlin)),
                    ),
                hub = "R8",
                outputs =
                    listOf(
                        FunnelNode("class files", Res.drawable.java),
                        FunnelNode("DEX files", Res.drawable.android),
                    ),
                caption = "one optimizer — two output formats",
            )
        }
    }
}

fun StoryboardBuilder.kotlinMultiplatformFunnel() {
    scene(
        frameCount = 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold {
            FunnelDiagram(
                inputs = listOf(FunnelNode("Kotlin", Res.drawable.kotlin)),
                hub = "Kotlin Multiplatform",
                hubLogo = Res.drawable.kmp,
                outputs =
                    listOf(
                        FunnelNode("JVM"),
                        FunnelNode("JS"),
                        FunnelNode("Wasm"),
                        FunnelNode("Native"),
                    ),
                caption = "one toolchain — many targets",
            )
        }
    }
}

fun StoryboardBuilder.llvmFunnel() {
    scene(
        frameCount = 1,
        enterTransition = SceneEnter(alignment = Alignment.CenterEnd),
        exitTransition = SceneExit(alignment = Alignment.CenterEnd),
    ) {
        SlideScaffold {
            FunnelDiagram(
                inputs =
                    listOf(
                        FunnelNode("Swift", Res.drawable.swift),
                        FunnelNode("Rust", Res.drawable.rust),
                        FunnelNode("Mono", Res.drawable.mono_gorilla),
                    ),
                hub = "LLVM",
                hubLogo = Res.drawable.llvm,
                outputs =
                    listOf(
                        FunnelNode("x64"),
                        FunnelNode("ARM"),
                        FunnelNode("RISC-V"),
                    ),
                caption = "many languages — one IR — many machines",
            )
        }
    }
}

@Composable
private fun FunnelDiagram(
    inputs: List<FunnelNode>,
    hub: String,
    outputs: List<FunnelNode>,
    caption: String,
    hubLogo: DrawableResource? = null,
) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            Modifier.weight(1f).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NodeColumn(inputs, Modifier.fillMaxHeight())
            FanLines(inputs.size, fanIn = true, Modifier.weight(1f).fillMaxHeight())
            Box(Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                Box(
                    Modifier
                        .border(4.dp, VgoGradient, RoundedCornerShape(16.dp))
                        .padding(horizontal = 32.dp, vertical = 24.dp),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        if (hubLogo != null) {
                            LogoTile(listOf(hubLogo))
                        }
                        GradientText(hub, style = MaterialTheme.typography.h4)
                    }
                }
            }
            FanLines(outputs.size, fanIn = false, Modifier.weight(1f).fillMaxHeight())
            NodeColumn(outputs, Modifier.fillMaxHeight())
        }

        Caption(caption)
    }
}

@Composable
private fun NodeColumn(
    nodes: List<FunnelNode>,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        for (node in nodes) {
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                if (node.logos.isNotEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        LogoTile(node.logos)
                        Text(node.label, color = VgoColors.Muted, style = MaterialTheme.typography.body2)
                    }
                } else {
                    OutlinedChip(node.label, color = VgoColors.Muted)
                }
            }
        }
    }
}

// A uniform light tile keeps differently sized (and black-fill) logos legible on the dark theme.
@Composable
private fun LogoTile(logos: List<DrawableResource>) {
    Row(
        Modifier
            .height(150.dp)
            .background(Color.White, RoundedCornerShape(24.dp))
            .padding(24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for (logo in logos) {
            Image(
                painterResource(logo),
                contentDescription = null,
                modifier = Modifier.size(102.dp),
            )
        }
    }
}

// Straight arrows between evenly weighted slots on one side and the hub's center on the other.
@Composable
private fun FanLines(
    count: Int,
    fanIn: Boolean,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier.padding(horizontal = 24.dp)) {
        val stroke = 4.dp.toPx()
        val headLength = 18.dp.toPx()
        val headSpread = 0.5f

        for (i in 0 until count) {
            val slotY = size.height * (i + 0.5f) / count
            val start = if (fanIn) Offset(0f, slotY) else Offset(0f, size.height / 2f)
            val end = if (fanIn) Offset(size.width, size.height / 2f) else Offset(size.width, slotY)

            drawLine(VgoColors.Muted, start, end, strokeWidth = stroke, cap = StrokeCap.Round)

            val angle = atan2(end.y - start.y, end.x - start.x)
            for (side in floatArrayOf(-headSpread, headSpread)) {
                val barb =
                    Offset(
                        end.x - headLength * cos(angle + side),
                        end.y - headLength * sin(angle + side),
                    )
                drawLine(VgoColors.Muted, end, barb, strokeWidth = stroke, cap = StrokeCap.Round)
            }
        }
    }
}
