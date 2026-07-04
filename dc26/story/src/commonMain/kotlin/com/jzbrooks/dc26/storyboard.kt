package com.jzbrooks.dc26

import com.jzbrooks.dc26.scenes.BakeTransformations
import com.jzbrooks.dc26.scenes.BakeTransformationsAlgorithm
import com.jzbrooks.dc26.scenes.Closing
import com.jzbrooks.dc26.scenes.CommandVariant
import com.jzbrooks.dc26.scenes.Conversion
import com.jzbrooks.dc26.scenes.CurvesToArcs
import com.jzbrooks.dc26.scenes.CurvesToArcsAlgorithm
import com.jzbrooks.dc26.scenes.KotlinMultiplatformFunnel
import com.jzbrooks.dc26.scenes.LlvmFunnel
import com.jzbrooks.dc26.scenes.MotivationCoffee
import com.jzbrooks.dc26.scenes.MotivationNode
import com.jzbrooks.dc26.scenes.MotivationTooling
import com.jzbrooks.dc26.scenes.MotivationVectorPain
import com.jzbrooks.dc26.scenes.VgoHistory
import com.jzbrooks.dc26.scenes.HowGraphicsWork
import com.jzbrooks.dc26.scenes.HowPathsWork
import com.jzbrooks.dc26.scenes.MergePaths
import com.jzbrooks.dc26.scenes.MergePathsAlgorithm
import com.jzbrooks.dc26.scenes.OptimizationCategories
import com.jzbrooks.dc26.scenes.Pipeline
import com.jzbrooks.dc26.scenes.PrintIr
import com.jzbrooks.dc26.scenes.R8Funnel
import com.jzbrooks.dc26.scenes.SimplifyBezierAlgorithm
import com.jzbrooks.dc26.scenes.SimplifyCommands
import com.jzbrooks.dc26.scenes.Title
import com.jzbrooks.dc26.scenes.UsingVgo
import com.jzbrooks.dc26.scenes.Vat
import com.jzbrooks.dc26.scenes.VgoName
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

        MotivationVectorPain()
        MotivationNode()
//        MotivationCoffee()
//        MotivationTooling()
        // TODO: Goal, build a tool than can shrink PDF or vector drawable illustrations

        section("What are vector graphics, actually?") {
            HowGraphicsWork()
            Conversion(false)
        }

        section("Which got me thinkin’") {
            R8Funnel()
            KotlinMultiplatformFunnel()
            LlvmFunnel()
            VgoName()
        }

        section("What is vgo?") {
            WhatIsVgo()
            Conversion(true)
            // TODO: Well, the IR approach worked out basically immediately. Dropped PDF for SVG on iOS 13
            PrintIr()
        }

        section("How Paths Work") {
            HowPathsWork()
        }

        section("Optimization Pipeline") {
            OptimizationCategories()
            Pipeline()
            CommandVariant()
            BakeTransformations()
            BakeTransformationsAlgorithm()
            SimplifyCommands()
            SimplifyBezierAlgorithm()
            CurvesToArcs()
            CurvesToArcsAlgorithm()
            MergePaths()
            MergePathsAlgorithm()
        }

        section("Using vgo") {
            UsingVgo()
        }

        section("Extras") {
            Vat()
        }

        Closing()
    }
}
