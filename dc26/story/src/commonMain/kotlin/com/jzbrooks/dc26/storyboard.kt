package com.jzbrooks.dc26

import com.jzbrooks.dc26.scenes.BakeTransformations
import com.jzbrooks.dc26.scenes.Closing
import com.jzbrooks.dc26.scenes.CommandVariant
import com.jzbrooks.dc26.scenes.Conversion
import com.jzbrooks.dc26.scenes.CurvesToArcs
import com.jzbrooks.dc26.scenes.DecompileApk
import com.jzbrooks.dc26.scenes.Hobby
import com.jzbrooks.dc26.scenes.HowGraphicsWork
import com.jzbrooks.dc26.scenes.HowPathsWork
import com.jzbrooks.dc26.scenes.MergePaths
import com.jzbrooks.dc26.scenes.OptimizationCategories
import com.jzbrooks.dc26.scenes.Pipeline
import com.jzbrooks.dc26.scenes.SimplifyCommands
import com.jzbrooks.dc26.scenes.Title
import com.jzbrooks.dc26.scenes.Vat
import com.jzbrooks.dc26.scenes.WhatIsVgo
import com.jzbrooks.dc26.theme.VgoTheme
import dev.bnorm.storyboard.SceneFormat
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.layout.template.section

fun createStoryboard(): Storyboard {
    return Storyboard.build(
        title = "vgo: A Vector Optimizer Built Like a Compiler",
        format = SceneFormat.Default,
        decorator = VgoTheme,
    ) {
        Title()

        section("Origins") {
            Hobby()
        }

        section("What is vgo?") {
            WhatIsVgo()
            HowGraphicsWork()
        }

        section("How paths work") {
            HowPathsWork()
        }

        section("Optimization") {
            OptimizationCategories()
            Pipeline()
            CommandVariant()
            BakeTransformations()
            MergePaths()
            SimplifyCommands()
            CurvesToArcs()
        }

        section("Conversion") {
            Conversion()
            DecompileApk()
        }

        section("Extras") {
            Vat()
        }

        Closing()
    }
}
