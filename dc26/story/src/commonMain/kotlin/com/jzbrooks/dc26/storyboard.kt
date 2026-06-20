package com.jzbrooks.dc26

import com.jzbrooks.dc26.scenes.BakeTransformations
import com.jzbrooks.dc26.scenes.Closing
import com.jzbrooks.dc26.scenes.CommandVariant
import com.jzbrooks.dc26.scenes.Conversion
import com.jzbrooks.dc26.scenes.CurvesToArcs
import com.jzbrooks.dc26.scenes.CurvesToArcsAlgorithm
import com.jzbrooks.dc26.scenes.DecompileApk
import com.jzbrooks.dc26.scenes.Hobby
import com.jzbrooks.dc26.scenes.VgoHistory
import com.jzbrooks.dc26.scenes.HowGraphicsWork
import com.jzbrooks.dc26.scenes.HowPathsWork
import com.jzbrooks.dc26.scenes.MergePaths
import com.jzbrooks.dc26.scenes.MergePathsAlgorithm
import com.jzbrooks.dc26.scenes.OptimizationCategories
import com.jzbrooks.dc26.scenes.Pipeline
import com.jzbrooks.dc26.scenes.SimplifyCommands
import com.jzbrooks.dc26.scenes.Title
import com.jzbrooks.dc26.scenes.UsingVgo
import com.jzbrooks.dc26.scenes.Vat
import com.jzbrooks.dc26.scenes.WhatIsVgo
import com.jzbrooks.dc26.theme.VgoTheme
import dev.bnorm.storyboard.SceneFormat
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.layout.Keynote
import dev.bnorm.storyboard.layout.template.section

fun createStoryboard(): Storyboard {
    return Storyboard.build(
        title = "Shrinking Vector Artwork",
        format = SceneFormat.Keynote,
        decorator = VgoTheme,
    ) {
        Title()

        section("Origins") {
            Hobby()
            Conversion(false)
        }

        section("What is vgo?") {
            WhatIsVgo()
            VgoHistory()
            HowGraphicsWork()
            HowPathsWork()
        }

        section("Optimization") {
            Pipeline()
            CommandVariant()
            BakeTransformations()
            MergePaths()
            MergePathsAlgorithm()
            SimplifyCommands()
            CurvesToArcs()
            CurvesToArcsAlgorithm()
        }

        section("Writing") {
            OptimizationCategories()
            Conversion(true)
        }

        DecompileApk()

        section("Using vgo") {
            UsingVgo()
        }

        section("Extras") {
            Vat()
        }

        Closing()
    }
}
